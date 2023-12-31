package com.xihua.easymqtt;

import com.xihua.easymqtt.common.ReqFuture;
import com.xihua.easymqtt.domain.Message;
import com.xihua.easymqtt.enums.MsgTypeEnum;
import com.xihua.easymqtt.exceptions.MqttServerConnectException;
import com.xihua.easymqtt.service.HandlerInterface;
import com.xihua.easymqtt.utils.MqttMessageUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MqttService {
    private MqttClient client;

    private final RequestManager requestManager = new RequestManager();

    private final MessageDispatcher dispatcher = new MessageDispatcher(this);

    private static final Map<String, MqttService> INSTANCE_MAP  = new ConcurrentHashMap<>(10);

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    private MqttService() {

    }

    private static String getKey(String brokerHost, String topic) {
        return brokerHost + topic;
    }

    protected static MqttService getInstance(String brokerHost, String topic, @Nullable String username, @Nullable String password, String persistenceDir, Object... args) throws MqttServerConnectException {
        String key = getKey(brokerHost, topic);
        if (!INSTANCE_MAP.containsKey(key)) {
            synchronized (MqttService.class) {
                if (!INSTANCE_MAP.containsKey(key)) {
                    MqttService instance = new MqttService();
                    instance.init(brokerHost, topic, username, password, persistenceDir);
                    INSTANCE_MAP.put(key, instance);
                }
            }
        }
        return INSTANCE_MAP.get(key);
    }

    protected boolean close(String brokerHost, String topic) throws MqttException {
        if (this.client != null) {
            synchronized (MqttService.class) {
                if (this.client != null) {
                    this.client.disconnect();
                    this.client.close();
                    this.client = null;
                    return INSTANCE_MAP.remove(getKey(brokerHost, topic)) != null;
                }
            }
        }
        return true;
    }

    protected void registerHandler(Class<? extends HandlerInterface> handlerClass) {
        dispatcher.register(handlerClass);
    }

    private void init(String brokerHost, String topic, @Nullable String username, @Nullable String password, String persistenceDir, Object... args) throws MqttServerConnectException {
        MqttDefaultFilePersistence persistence = new MqttDefaultFilePersistence(persistenceDir);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setConnectionTimeout(10);
        connOpts.setAutomaticReconnect(true);
        connOpts.setKeepAliveInterval(60);
        connOpts.setCleanSession(true);
        if (username != null) {
            connOpts.setUserName(username);
        }
        if (password != null) {
            connOpts.setPassword(password.toCharArray());
        }

        try {
            client = new MqttClient(brokerHost, MqttClient.generateClientId(), persistence);
            client.setCallback(new MqttReceiverCallback(dispatcher));
            client.setTimeToWait(10 * 1000);
            logger.info("Connecting to broker: " + brokerHost);
            client.connect(connOpts);
            logger.info("Connected to broker: " + brokerHost);
            // 订阅 topic
            client.subscribe(topic, 2);

        } catch (MqttException e) {
            throw new MqttServerConnectException(e);
        }
    }

    protected ReqFuture send(Message message) {
        if (Objects.equals(message.getMsgType(), MsgTypeEnum.RESPONSE.getMsgType())) {
            send(MqttMessageUtil.getMqttMessage(message), message.getTargetTopic(), 2);
            return null;
        }
        if (Objects.equals(message.getMsgType(), MsgTypeEnum.REQUEST.getMsgType())) {
            requestManager.add(message.getReqId());
            send(MqttMessageUtil.getMqttMessage(message), message.getTargetTopic(), 2);
            return requestManager.get(message.getReqId());
        }
        return null;
    }

    private void send(MqttMessage message, String topic, int qos) {
        // 发布消息
        message.setQos(qos);
        try {
            client.publish(topic, message);
            logger.info("Message published");
//            client.disconnect();
//            logger.info("Disconnected");
//            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
