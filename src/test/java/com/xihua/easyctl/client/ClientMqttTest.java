package com.xihua.easyctl.client;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.MClient;
import com.xihua.easyctl.MqttService;
import com.xihua.easyctl.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClientMqttTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientMqttTest.class);

    public static void main(String[] args) throws InterruptedException {
        MqttService instance = MqttService.getInstance("tcp://192.168.1.4:1883", "/cli/2");

        MClient mClient = new MClient("tcp://192.168.1.4:1883", "/cli/2");

        List<String> params = new ArrayList<>();
        params.add("req");
        params.add("send");
        Message resp = mClient.call("/cli/1", "/hello", params);
//        Message resp = mClient.call("/cli/2", "/hello", params);
        logger.info("call response = " + JSON.toJSONString(resp));

        while (true) {

        }

    }
}
