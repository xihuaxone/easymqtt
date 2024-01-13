package com.xihua.easymqtt.client;

import com.alibaba.fastjson.JSON;
import com.xihua.easymqtt.MClient;
import com.xihua.easymqtt.domain.Message;
import com.xihua.easymqtt.exceptions.MqttServerConnectException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClientMqttTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientMqttTest.class);

    @Test
    public void test() throws MqttServerConnectException, InterruptedException {
        logger.info("start...");
        MClient mClient = new MClient("tcp://192.168.1.7:1883", "/cli/1", "root", "13241324", "./");

        List<String> params = new ArrayList<>();
        params.add("req");
        params.add("send");
        Message resp = mClient.call("/tmn/13392590", "/healthcheck", params);
        logger.info("call response = " + JSON.toJSONString(resp));

        while (true) {
        }

    }
}
