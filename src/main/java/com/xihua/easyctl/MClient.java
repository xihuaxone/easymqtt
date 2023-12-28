package com.xihua.easyctl;

import com.xihua.easyctl.common.ReqFuture;
import com.xihua.easyctl.domain.Message;
import com.xihua.easyctl.enums.MsgTypeEnum;
import com.xihua.easyctl.exceptions.MWaitResonseException;
import com.xihua.easyctl.exceptions.MqttServerConnectException;
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

    private final MqttService service;

    private static final Logger logger = LoggerFactory.getLogger(MClient.class);

    public MClient(String brokerHost, String sourceTopic) throws MqttServerConnectException {
        this.brokerHost = brokerHost;
        this.sourceTopic = sourceTopic;
        service = MqttService.getInstance(brokerHost, sourceTopic, null, null);
    }

    public MClient(String brokerHost, String sourceTopic, String username, String password, Object... args) throws MqttServerConnectException {
        this.brokerHost = brokerHost;
        this.sourceTopic = sourceTopic;
        service = MqttService.getInstance(brokerHost, sourceTopic, username, password, args);
    }

    public Message call(String targetTopic, String api, List<String> params) {
        ReqFuture future = service.send(new Message(generateReqId(), sourceTopic, targetTopic,
                MsgTypeEnum.REQUEST.getMsgType(), api, params));

        try {
            return future.get(1000 * 5, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            logger.error("call remote error: " + e);
            throw new MWaitResonseException(e);
        }
    }

    private String generateReqId() {
        return String.valueOf(new Random().nextInt(999999999));
    }
}
