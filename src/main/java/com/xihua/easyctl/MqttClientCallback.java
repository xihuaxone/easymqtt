package com.xihua.easyctl;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.domain.MRequest;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttClientCallback implements MqttCallback {

    // 连接丢失
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connection lost：" + cause.getMessage());
    }

    //  收到消息
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        byte[] payload = message.getPayload();
        byte msgType = payload[0];

        byte[] content = new byte[payload.length - 1];
        System.arraycopy(payload, 1, content, 0, payload.length - 1);
        String[] contentStr = new String(content).split("\\|");

        String sourceTopic = contentStr[0].trim();

        String[] params = new String[contentStr.length - 1];
        System.arraycopy(contentStr, 1, params, 0, contentStr.length - 1);

        MRequest mRequest = new MRequest(sourceTopic, topic, msgType, params);

        System.out.println("[" + topic + "] receive msg from [" + sourceTopic + "] : " + JSON.toJSONString(mRequest));

    }

    // 消息传递成功
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete");
    }
}
