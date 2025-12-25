package com.xihua.easymqtt.example;

import com.alibaba.fastjson.JSON;
import com.xihua.easymqtt.MClient;
import com.xihua.easymqtt.domain.Message;
import com.xihua.easymqtt.exceptions.MqttServerConnectException;
import com.xihua.easymqtt.exceptions.MWaitResonseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的客户端示例
 * 启动后会向服务端发送 RPC 调用
 */
public class SimpleClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleClient.class);

    public static void main(String[] args) {
        try {
            // 创建 MQTT 客户端
            // 参数：broker地址, 订阅的topic, 用户名, 密码, 持久化目录
            MClient client = new MClient(
                    "tcp://localhost:1883",  // MQTT broker 地址
                    "/client/1",             // 本客户端订阅的 topic
                    null,                    // 用户名（如果不需要认证）
                    null,                    // 密码（如果不需要认证）
                    "./mqtt-persistence"     // 持久化目录
            );

            logger.info("客户端已连接");

            // 示例1: 调用 /hello API
            logger.info("\n=== 调用 /hello API ===");
            List<Object> params1 = new ArrayList<>();
            Message response1 = client.call("/server/1", "/hello", params1);
            logger.info("响应: {}", JSON.toJSONString(response1));

            Thread.sleep(1000);

            // 示例2: 调用 /echo API
            logger.info("\n=== 调用 /echo API ===");
            List<Object> params2 = new ArrayList<>();
            params2.add("Hello");
            params2.add("World");
            params2.add(123);
            Message response2 = client.call("/server/1", "/echo", params2);
            logger.info("响应: {}", JSON.toJSONString(response2));

            // 关闭连接
            client.close();
            logger.info("客户端已关闭");

        } catch (MqttServerConnectException e) {
            logger.error("连接 MQTT broker 失败", e);
            logger.error("请确保 MQTT broker 正在运行，默认地址: tcp://localhost:1883");
        } catch (MWaitResonseException e) {
            logger.error("等待响应超时", e);
            logger.error("请确保服务端已启动并注册了对应的处理器");
        } catch (Exception e) {
            logger.error("发生错误", e);
        }
    }
}

