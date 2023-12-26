package com.xihua.easyctl.client;

import com.xihua.easyctl.MqttService;

public class ClientMqttTest2 {
    public static void main(String[] args) throws InterruptedException {
        MqttService.getInstance("tcp://192.168.1.4:1883", "/cli/2");
        while (true) {

        }

    }
}
