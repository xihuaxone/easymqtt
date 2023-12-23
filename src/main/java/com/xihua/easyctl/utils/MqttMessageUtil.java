package com.xihua.easyctl.utils;

import com.xihua.easyctl.domain.MRequest;
import com.xihua.easyctl.domain.MResponse;
import com.xihua.easyctl.domain.Message;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.ByteBuffer;

public class MqttMessageUtil {
    public static MqttMessage getMqttMessage(Message message) {
        return getMqttMessage(message.getReqId(), message.getSourceTopic(), message.getMsgType(), message.getApi(), message.getParams());
    }

    public static MqttMessage getMqttMessage(int reqId, String sourceTopic, byte msgType, String api, String[] params) {
        String[] contentStr = new String[params.length + 2];
        contentStr[0] = sourceTopic;
        contentStr[1] = api;
        System.arraycopy(params, 0, contentStr, 2, params.length);

        byte[] content = String.join("|", contentStr).getBytes();
        byte[] payload = new byte[content.length + 5];

        payload[0] = msgType;
        System.arraycopy(ByteBuffer.allocate(4).putInt(reqId).array(), 0, payload, 1, 4);

        System.arraycopy(content, 0, payload, 5, content.length);

        return new MqttMessage(payload);
    }

    public static Message getMessage(MqttMessage message, String targetTopic) {
        byte[] payload = message.getPayload();

        byte msgType = payload[0];

        byte[] reqIdBytes = new byte[4];
        System.arraycopy(payload, 1, reqIdBytes, 0, 4);
        int reqId = ByteBuffer.wrap(reqIdBytes).getInt();

        byte[] content = new byte[payload.length - 5];
        System.arraycopy(payload, 5, content, 0, content.length);
        String[] contentStr = new String(content).split("\\|");

        String sourceTopic = contentStr[0].trim();

        String api = contentStr[1].trim();

        String[] params = new String[contentStr.length - 2];
        System.arraycopy(contentStr, 2, params, 0, params.length);


        return new Message(reqId, sourceTopic, targetTopic, msgType, api, params);
    }
}
