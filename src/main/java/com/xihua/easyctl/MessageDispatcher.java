package com.xihua.easyctl;

import com.alibaba.fastjson.JSON;
import com.xihua.easyctl.annocation.MService;
import com.xihua.easyctl.domain.Message;
import com.xihua.easyctl.enums.MsgTypeEnum;
import com.xihua.easyctl.service.HandlerInterface;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.*;

public class MessageDispatcher {
    private final RequestManager requestManager = new RequestManager();

    private static Map<String, Class<? extends HandlerInterface>> HANDLER_MAP = new HashMap<>(20);

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(32, 32,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    static {
        register();
    }

    public static void register() {
        for (Class<?> c : new Reflections("com.xihua.easyctl.service").getTypesAnnotatedWith(MService.class)) {
            if (c.isAnnotationPresent(MService.class)) {
                for (Class<?> anInterface : c.getInterfaces()) {
                    if (anInterface.equals(HandlerInterface.class)) {
                        MService annotation = c.getAnnotation(MService.class);
                        HANDLER_MAP.put(annotation.api(), (Class<? extends HandlerInterface>) c);
                        System.out.println("api [" + annotation.api() + "] bounded handler [" + c.getName() + "] registered.");
                        break;
                    }
                }
            }
        }
    }

    public void dispatch(Message message) {
        if (Objects.equals(message.getMsgType(), MsgTypeEnum.REQUEST.getMsgType())) {
            dispatchRequest(message);
        } else if (Objects.equals(message.getMsgType(), MsgTypeEnum.RESPONSE.getMsgType())) {
            dispatchResponse(message);
        }
    }

    private void dispatchRequest(Message message) {
        System.out.println("request = " + JSON.toJSONString(message));
        String api = message.getApi();

        EXECUTOR.submit(() -> {
            Class<? extends HandlerInterface> handler = HANDLER_MAP.getOrDefault(api, DefaultHandler.class);
            Message response;
            try {
                List<String> respParams = handler.newInstance().handle(message.getParams());
                response = new Message(message.getReqId(), message.getTargetTopic(),
                        message.getSourceTopic(), MsgTypeEnum.RESPONSE.getMsgType(), message.getApi(), respParams);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (Throwable e) {
                System.out.println("handle request error: " + e);
                throw new RuntimeException(e);
            }

            System.out.println("response = " + JSON.toJSONString(response));
            MqttService.getInstance("tcp://192.168.1.4:1883", message.getTargetTopic()).send(response);
        });
    }

    private void dispatchResponse(Message message) {
        System.out.println("response = " + JSON.toJSONString(message));
        requestManager.updateResult(message.getReqId(), message);
    }

    static class DefaultHandler implements HandlerInterface {
        @Override
        public List<String> handle(List<String> params) {
            return new ArrayList<>(0);
        }
    }
}
