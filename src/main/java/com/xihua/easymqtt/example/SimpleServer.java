package com.xihua.easymqtt.example;

import com.xihua.easymqtt.MClient;
import com.xihua.easymqtt.annocation.MService;
import com.xihua.easymqtt.exceptions.MqttServerConnectException;
import com.xihua.easymqtt.service.HandlerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 简单的服务端示例
 * 启动后会注册处理器，等待接收请求
 */
public class SimpleServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);

    public static void main(String[] args) {
        try {
            // 创建 MQTT 客户端
            // 参数：broker地址, 订阅的topic, 用户名, 密码, 持久化目录
            MClient client = new MClient(
                    "tcp://localhost:1883",  // MQTT broker 地址
                    "/server/1",              // 本服务订阅的 topic
                    null,                     // 用户名（如果不需要认证）
                    null,                     // 密码（如果不需要认证）
                    "./mqtt-persistence"      // 持久化目录
            );

            // 注册服务处理器
            client.registerHandler(HelloHandler.class);
            client.registerHandler(EchoHandler.class);

            logger.info("服务器已启动，等待接收请求...");
            logger.info("订阅 topic: /server/1");
            logger.info("注册的 API: /hello, /echo");

            // 保持程序运行
            Thread.sleep(Long.MAX_VALUE);
        } catch (MqttServerConnectException e) {
            logger.error("连接 MQTT broker 失败", e);
        } catch (InterruptedException e) {
            logger.error("程序被中断", e);
        }
    }

    @MService(api = "/hello")
    public static class HelloHandler implements HandlerInterface {
        @Override
        public List<Object> handle(List<Object> params) {
            logger.info("收到 /hello 请求，参数: {}", params);
            return Collections.singletonList("Hello from EasyMQTT Server!");
        }
    }

    @MService(api = "/echo")
    public static class EchoHandler implements HandlerInterface {
        @Override
        public List<Object> handle(List<Object> params) {
            logger.info("收到 /echo 请求，参数: {}", params);
            // 返回接收到的参数
            return params;
        }
    }
}

