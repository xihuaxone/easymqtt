package com.xihua.easyctl;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttReceiver {
    private final String brokerHost;

    private final String topic;

    private MqttClient client;

    public MqttReceiver(String brokerHost, String topic) {
        this.brokerHost = brokerHost;
        this.topic = topic;
    }

    private static void subscribe(MqttClient client) throws MqttException {
//        for (Class<?> c : new Reflections("com.xihua.easyctl").getTypesAnnotatedWith(MController.class)) {
//            if (c.isAnnotationPresent(MController.class)) {
//                MController annotation = c.getAnnotation(MController.class);
//                client.subscribe(annotation.topic(), annotation.qos());
//                System.out.println("Subscribed to topic: " + annotation.topic());
//            }
//        }
    }

    public void init() {
        //  持久化
        MemoryPersistence persistence = new MemoryPersistence();
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置认证信息
//        connOpts.setUserName("burt");
//        connOpts.setPassword("burt".toCharArray());

        try {
            client = new MqttClient(brokerHost, MqttClient.generateClientId(), persistence);
            // 设置回调
            client.setCallback(new MqttClientCallback());
            // 建立连接
            System.out.println("Connecting to broker: " + brokerHost);
            client.connect(connOpts);
            System.out.println("Connected to broker: " + brokerHost);
            // 订阅 topic
            client.subscribe(topic, 2);

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
