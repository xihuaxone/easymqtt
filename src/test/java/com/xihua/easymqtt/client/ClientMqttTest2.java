package com.xihua.easymqtt.client;

import com.xihua.easymqtt.MClient;
import com.xihua.easymqtt.annocation.MService;
import com.xihua.easymqtt.exceptions.MqttServerConnectException;
import com.xihua.easymqtt.service.HandlerInterface;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ClientMqttTest2 {

    @Test
    public void test() throws MqttServerConnectException {
        MClient client = new MClient("tcp://192.168.1.4:1883", "/cli/2", "root", "13241324", "./");

        client.registerHandler(Handler.class);
        while (true) {

        }
    }

    @MService(api = "/test")
    public static class Handler implements HandlerInterface {
        @Override
        public List<String> handle(List<String> params) {
            return Collections.singletonList("ok");
        }
    }
}
