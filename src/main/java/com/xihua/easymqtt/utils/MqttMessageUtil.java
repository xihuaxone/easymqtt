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
        JSONObject json = (JSONObject) JSON.toJSON(message);
        json.put("params", String.join(",", message.getParams()));  // 终端设备程序暂时不会做json内不定长数组的动态提取，先转为字符串传参，曲线救国一下；
        payload = JSON.toJSONString(json).getBytes();
        return new MqttMessage(payload);
    }

    private static Message mqttMessage2Message(MqttMessage mqttMessage) {
        JSONObject json = JSON.parseObject(mqttMessage.getPayload(), JSONObject.class);
        json.put("params", Arrays.stream(json.getString("params").split(",")).collect(Collectors.toList()));
        return json.toJavaObject(Message.class);
    }
//
//    private static MqttMessage getMqttMessage(int reqId, String sourceTopic, byte msgType, String api, String[] params) {
//        String[] contentStr = new String[params.length + 2];
//        contentStr[0] = sourceTopic;
//        contentStr[1] = api;
//        System.arraycopy(params, 0, contentStr, 2, params.length);
//
//        byte[] content = String.join("|", contentStr).getBytes();
//        byte[] payload = new byte[content.length + 5];
//
//        payload[0] = msgType;
//        System.arraycopy(ByteBuffer.allocate(4).putInt(reqId).array(), 0, payload, 1, 4);
//
//        System.arraycopy(content, 0, payload, 5, content.length);
//
//        return new MqttMessage(payload);
//    }

//    public static Message getMessage(MqttMessage message, String targetTopic) {
//        byte[] payload = message.getPayload();
//
//        byte msgType = payload[0];
//
//        byte[] reqIdBytes = new byte[4];
//        System.arraycopy(payload, 1, reqIdBytes, 0, 4);
//        int reqId = ByteBuffer.wrap(reqIdBytes).getInt();
//
//        byte[] content = new byte[payload.length - 5];
//        System.arraycopy(payload, 5, content, 0, content.length);
//        String[] contentStr = new String(content).split("\\|");
//
//        String sourceTopic = contentStr[0].trim();
//
//        String api = contentStr[1].trim();
//
//        String[] params = new String[contentStr.length - 2];
//        System.arraycopy(contentStr, 2, params, 0, params.length);
//
//
//        return new Message(reqId, sourceTopic, targetTopic, msgType, api, params);
//    }
}
