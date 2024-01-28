package com.xihua.easymqtt;

import com.xihua.easymqtt.common.ReqFuture;
import com.xihua.easymqtt.domain.Message;
import com.xihua.easymqtt.enums.MsgTypeEnum;
import com.xihua.easymqtt.exceptions.MWaitResonseException;
import com.xihua.easymqtt.exceptions.MqttServerConnectException;
import com.xihua.easymqtt.service.HandlerInterface;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MClient {

    private final String brokerHost;

    private final String sourceTopic;

    private final MqttService service;

    private static final Logger logger = LoggerFactory.getLogger(MClient.class);

    public MClient(String brokerHost, String sourceTopic, String persistenceDir) throws MqttServerConnectException {
        this.brokerHost = brokerHost;
        this.sourceTopic = sourceTopic;
        service = MqttService.getInstance(brokerHost, sourceTopic, null, null, persistenceDir);
    }

    public MClient(String brokerHost, String sourceTopic, String username, String password, String persistenceDir, Object... args) throws MqttServerConnectException {
        this.brokerHost = brokerHost;
        this.sourceTopic = sourceTopic;
        service = MqttService.getInstance(brokerHost, sourceTopic, username, password, persistenceDir, args);
    }

    public MClient registerHandler(Class<? extends HandlerInterface> handlerClass) {
        service.registerHandler(handlerClass);
        return this;
    }

    public Message call(String targetTopic, String api, List<Object> params) {
        ReqFuture future = service.send(new Message(generateReqId(), sourceTopic, targetTopic,
                MsgTypeEnum.REQUEST.getMsgType(), api, params));

        try {
            return future.get(1000 * 5, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            logger.error("call remote error: " + e);
            throw new MWaitResonseException(e);
        }
    }

    public boolean close() {
        try {
            return service.close();
        } catch (MqttException e) {
            logger.error("close mqtt error: ", e);
            return false;
        }
    }

    private String generateReqId() {
        return String.valueOf(new Random().nextInt(999999999));
    }
}
