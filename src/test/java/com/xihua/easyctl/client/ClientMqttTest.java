package com.xihua.easyctl.client;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.MClient;
import com.xihua.easyctl.MqttService;
import com.xihua.easyctl.domain.Message;

public class ClientMqttTest {
    public static void main(String[] args) throws InterruptedException {
        MqttService instance = MqttService.getInstance("tcp://192.168.1.4:1883", "/cli/1");

        MClient mClient = new MClient("tcp://192.168.1.4:1883", "/cli/1");

        String[] params = new String[2];
        params[0] = "req";
        params[1] = "send";
        Message resp = mClient.call("/cli/2", "/hello", params);
        System.out.println("call response = " + JSON.toJSONString(resp));

        while (true) {

        }

    }
}
