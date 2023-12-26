package com.xihua.easyctl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        String topic = "/cli/1";
        logger.info("start...");
        MqttService instance = MqttService.getInstance("tcp://192.168.1.4:1883", topic);
    }
}
