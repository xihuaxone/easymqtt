package com.xihua.easymqtt.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xihua.easymqtt.domain.Message;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MqttMessageUtil {
    public static MqttMessage getMqttMessage(Message message) {
        return message2MqttMessage(message);
    }

    public static Message getMessage(MqttMessage mqttMessage, String targetTopic) {
        Message message = mqttMessage2Message(mqttMessage);
        message.setTargetTopic(targetTopic);
        return message;
    }

    private static MqttMessage message2MqttMessage(Message message) {
        byte[] payload;
        payload = JSON.toJSONString(message).getBytes();
        return new MqttMessage(payload);
    }

    private static Message mqttMessage2Message(MqttMessage mqttMessage) {
        JSONObject json = JSON.parseObject(mqttMessage.getPayload(), JSONObject.class);
        return json.toJavaObject(Message.class);
    }
}
