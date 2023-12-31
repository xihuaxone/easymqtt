package com.xihua.easymqtt;

import com.alibaba.fastjson.JSON;
import com.xihua.easymqtt.domain.Message;
import com.xihua.easymqtt.utils.MqttMessageUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttReceiverCallback implements MqttCallback {
    private final MqttService.ConnectionLostCallback connectionLostCallback;

    private static final Logger logger = LoggerFactory.getLogger(MqttReceiverCallback.class);

    private final MessageDispatcher messageDispatcher;

    public MqttReceiverCallback(MessageDispatcher messageDispatcher, MqttService.ConnectionLostCallback connectionLostCallback) {
        this.messageDispatcher = messageDispatcher;
        this.connectionLostCallback = connectionLostCallback;
    }

    // 连接丢失
    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("connection lost：" + cause.getMessage());
        try {
            connectionLostCallback.call();
        } catch (MqttException e) {
            logger.warn("connectionLostCallback call error: ", e);
        }
    }

    //  收到消息
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Message msg;
        try {
            msg = MqttMessageUtil.getMessage(message, topic);
        } catch (Throwable e) {
            logger.info("[" + topic + "] receive unrecognized msg: " + JSON.toJSONString(message.getPayload()));
            return;
        }
        if (messageDispatcher != null) {
            messageDispatcher.dispatch(msg);
        }
    }

    // 消息传递成功
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("delivery complete");
    }
}
