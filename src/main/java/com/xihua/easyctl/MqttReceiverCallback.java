package com.xihua.easyctl;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.domain.Message;
import com.xihua.easyctl.utils.MqttMessageUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttReceiverCallback implements MqttCallback {
    private final MessageDispatcher messageDispatcher;

    public MqttReceiverCallback(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    // 连接丢失
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connection lost：" + cause.getMessage());
    }

    //  收到消息
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Message msg;
        try {
            msg = MqttMessageUtil.getMessage(message, topic);
        } catch (Throwable e) {
            System.out.println("[" + topic + "] receive unrecognized msg: " + JSON.toJSONString(message.getPayload()));
            return;
        }
        if (messageDispatcher != null) {
            messageDispatcher.dispatch(msg);
        }
    }

    // 消息传递成功
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("delivery complete");
    }
}
