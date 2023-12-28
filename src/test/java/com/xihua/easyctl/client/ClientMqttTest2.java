package com.xihua.easyctl.client;

import com.xihua.easyctl.MClient;
import com.xihua.easyctl.exceptions.MqttServerConnectException;
import org.junit.Test;

public class ClientMqttTest2 {

    @Test
    public void test() throws MqttServerConnectException {
        new MClient("tcp://192.168.1.4:1883", "/cli/2", "usr", "pwd");
        while (true) {

        }
    }
}
