package com.xihua.easyctl;

import com.xihua.easyctl.common.ReqFuture;
import com.xihua.easyctl.domain.Message;
import com.xihua.easyctl.enums.MsgTypeEnum;
import com.xihua.easyctl.utils.MqttMessageUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MqttService {
    private MqttClient client;

    private final RequestManager requestManager = new RequestManager();

    private static final Map<String, MqttService> INSTANCE_MAP  = new ConcurrentHashMap<>(10);

    private MqttService() {

    }

    private static String getKey(String brokerHost, String topic) {
        return brokerHost + topic;
    }

    public static MqttService getInstance(String brokerHost, String topic) {
        String key = getKey(brokerHost, topic);
        if (!INSTANCE_MAP.containsKey(key)) {
            synchronized (MqttService.class) {
                if (!INSTANCE_MAP.containsKey(key)) {
                    MqttService instance = new MqttService();
                    instance.init(brokerHost, topic);
                    INSTANCE_MAP.put(key, instance);
                }
            }
        }
        return INSTANCE_MAP.get(key);
    }

    private void init(String brokerHost, String topic) {

        //  持久化
        MemoryPersistence persistence = new MemoryPersistence();
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置认证信息
//        connOpts.setUserName("burt");
//        connOpts.setPassword("burt".toCharArray());

        try {
            client = new MqttClient(brokerHost, MqttClient.generateClientId(), persistence);
            // 设置回调
            client.setCallback(new MqttReceiverCallback(new MessageDispatcher()));
            // 建立连接
            System.out.println("Connecting to broker: " + brokerHost);
            client.connect(connOpts);
            System.out.println("Connected to broker: " + brokerHost);
            // 订阅 topic
            client.subscribe(topic, 2);

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    protected ReqFuture send(Message message) {
        if (message.getMsgType() == MsgTypeEnum.RESPONSE.getMsgType()) {
            send(MqttMessageUtil.getMqttMessage(message), message.getTargetTopic(), 2);
            return null;
        }
        if (message.getMsgType() == MsgTypeEnum.REQUEST.getMsgType()) {
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
            System.out.println("Message published");
//            client.disconnect();
//            System.out.println("Disconnected");
//            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}