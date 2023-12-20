package com.xihua.easyctl.client;

import com.xihua.easyctl.MqttReceiver;
import com.xihua.easyctl.MqttSender;
import com.xihua.easyctl.domain.MRequest;
import com.xihua.easyctl.domain.MResponse;

public class ClientMqttTest {
    public static void main(String[] args) throws InterruptedException {
        Thread threadRev = new Thread(new Runnable() {
            @Override
            public void run() {
                new MqttReceiver("tcp://192.168.1.4:1883", "/cli/1").init();
            }
        });

        threadRev.setDaemon(true);
        threadRev.start();

        Thread.sleep(1000 * 2);

        {
            String[] params = new String[2];
            params[0] = "req";
            params[1] = "send";
            MRequest mRequest = new MRequest("/cli/2", "/cli/1", (byte) 1, params);
            new MqttSender("tcp://192.168.1.4:1883", "/demo").send(mRequest);
        }

        {
            String[] params = new String[2];
            params[0] = "resp";
            params[1] = "send";
            MResponse mResponse = new MResponse("/cli/2", "/cli/1", (byte) 1, params);
            new MqttSender("tcp://192.168.1.4:1883", "/demo").send(mResponse);
        }

        while (true) {

        }

    }
}
