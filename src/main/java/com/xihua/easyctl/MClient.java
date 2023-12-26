package com.xihua.easyctl;

import com.xihua.easyctl.common.ReqFuture;
import com.xihua.easyctl.domain.Message;
import com.xihua.easyctl.enums.MsgTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MClient {

    private final String brokerHost;

    private final String sourceTopic;

    private static final Logger logger = LoggerFactory.getLogger(MClient.class);

    public MClient(String brokerHost, String sourceTopic) {
        this.brokerHost = brokerHost;
        this.sourceTopic = sourceTopic;
    }

    public Message call(String targetTopic, String api, List<String> params) {
        MqttService service = MqttService.getInstance(brokerHost, sourceTopic);

        ReqFuture future = service.send(new Message(generateReqId(), sourceTopic, targetTopic,
                MsgTypeEnum.REQUEST.getMsgType(), api, params));

        try {
            return future.get(1000 * 5, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            logger.error("call remote error: " + e);
            throw new RuntimeException(e);
        }
    }

    private String generateReqId() {
        return String.valueOf(new Random().nextInt(999999999));
    }
}
