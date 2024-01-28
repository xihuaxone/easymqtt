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

        List<Object> params = new ArrayList<>();
        params.add(false);

//        Message resp1 = mClient.call("/tmn/14444547", "/light/step", params);
        Message resp1 = mClient.call("/tmn/14444547", "/light/on", params);
        logger.info("call response = " + JSON.toJSONString(resp1));


//        while (true) {
//        }

    }
}
