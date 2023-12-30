package com.xihua.easymqtt.exceptions;

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
