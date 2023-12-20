package com.xihua.easyctl;

import com.xihua.easyctl.domain.MRequest;
import com.xihua.easyctl.domain.MResponse;
import com.xihua.easyctl.utils.MqttMessageUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSender {
    private final String brokerHost;

    private final String topic;

    private MqttClient client;

    public MqttSender(String brokerHost, String topic) {
        this.brokerHost = brokerHost;
        this.topic = topic;
        try {
            init();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws MqttException {
        //  持久化
        MemoryPersistence persistence = new MemoryPersistence();
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置认证信息
        // connOpts.setUserName("burt");
        // connOpts.setPassword("burt".toCharArray());
        client = new MqttClient(brokerHost, MqttClient.generateClientId(), persistence);
        // 设置回调
        client.setCallback(new MqttClientCallback());
        // 建立连接
        System.out.println("Connecting to broker: " + brokerHost);
        client.connect(connOpts);
    }

    public void send(MRequest request) {
        send(MqttMessageUtil.getMqttMessage(request), request.getTargetTopic(), 2);
    }

    public void send(MResponse response) {
        send(MqttMessageUtil.getMqttMessage(response), response.getTargetTopic(), 2);
    }

    public void send(MqttMessage message, String topic, int qos) {
        // 发布消息
        message.setQos(qos);
        try {
            client.publish(topic, message);
            System.out.println("Message published");
            client.disconnect();
            System.out.println("Disconnected");
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
