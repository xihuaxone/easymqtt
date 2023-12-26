package com.xihua.easyctl.client;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.MClient;
import com.xihua.easyctl.MqttService;
import com.xihua.easyctl.domain.Message;

import java.util.ArrayList;
import java.util.List;

public class ClientMqttTest {
    public static void main(String[] args) throws InterruptedException {
        MqttService instance = MqttService.getInstance("tcp://192.168.1.4:1883", "/cli/1");

        MClient mClient = new MClient("tcp://192.168.1.4:1883", "/cli/1");

        List<String> params = new ArrayList<>();
        params.add("req");
        params.add("send");
        Message resp = mClient.call("/test/device1", "/healthcheck", params);
//        Message resp = mClient.call("/cli/2", "/hello", params);
        logger.info("call response = " + JSON.toJSONString(resp));

        while (true) {

        }

    }
}
