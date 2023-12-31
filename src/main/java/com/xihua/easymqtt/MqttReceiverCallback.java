package com.xihua.easymqtt;

import com.alibaba.fastjson.JSON;
import com.xihua.easymqtt.domain.Message;
import com.xihua.easymqtt.utils.MqttMessageUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttReceiverCallback implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MqttReceiverCallback.class);

    private final MessageDispatcher messageDispatcher;

    public MqttReceiverCallback(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    // 连接丢失
    @Override
    public void connectionLost(Throwable cause) {
        logger.info("connection lost：" + cause.getMessage());
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