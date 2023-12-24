package com.xihua.easyctl.client;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.MClient;
import com.xihua.easyctl.MqttService;
import com.xihua.easyctl.domain.Message;

public class ClientMqttTest2 {
    public static void main(String[] args) throws InterruptedException {
        MqttService instance = MqttService.getInstance("tcp://192.168.1.4:1883", "/cli/2");

        MClient mClient = new MClient("tcp://192.168.1.4:1883", "/cli/2");

        while (true) {

        }

    }
}
