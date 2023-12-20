package com.xihua.easyctl.utils;

import com.xihua.easyctl.domain.MRequest;
import com.xihua.easyctl.domain.MResponse;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageUtil {

    public static MqttMessage getMqttMessage(MRequest request) {
        return getMqttMessage(request.getSourceTopic(), request.getMsgType(), request.getParams());
    }

    public static MqttMessage getMqttMessage(MResponse response) {
        return getMqttMessage(response.getSourceTopic(), response.getMsgType(), response.getParams());
    }

    public static MqttMessage getMqttMessage(String sourceTopic, byte msgType, String[] params) {
        String[] contentStr = new String[params.length + 1];
        contentStr[0] = sourceTopic;
        System.arraycopy(params, 0, contentStr, 1, params.length);

        byte[] content = String.join("\\|", contentStr).getBytes();
        byte[] payload = new byte[content.length + 1];
        payload[0] = msgType;
        System.arraycopy(content, 0, payload, 1, content.length);

        return new MqttMessage(payload);
    }
}
