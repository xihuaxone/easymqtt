package com.xihua.easyctl.exceptions;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MWaitResonseException extends RuntimeException {
    public MWaitResonseException() {
        super();
    }

    public MWaitResonseException(Throwable e) {
        super(e);
    }

    public MWaitResonseException(String message) {
        super(message);
    }

}
