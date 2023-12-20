package com.xihua.easyctl.domain;

public class MResponse {
    private String sourceTopic;

    private String targetTopic;

    private byte msgType;

    private String[] params;

    public MResponse(String sourceTopic, String targetTopic, byte msgType, String[] params) {
        this.sourceTopic = sourceTopic;
        this.targetTopic = targetTopic;
        this.msgType = msgType;
        this.params = params;
    }

    public String getSourceTopic() {
        return sourceTopic;
    }

    public String getTargetTopic() {
        return targetTopic;
    }

    public byte getMsgType() {
        return msgType;
    }

    public String[] getParams() {
        return params;
    }
}
