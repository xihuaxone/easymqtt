package com.xihua.easyctl.exceptions;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttServerConnectException extends Exception {
    public MqttServerConnectException() {
        super();
    }

    public MqttServerConnectException(Throwable e) {
        super(e);
    }

    public MqttServerConnectException(String message) {
        super(message);
    }
}
