package com.xihua.easyctl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Message {
    private final int reqId;
    private final String sourceTopic;

    private final String targetTopic;

    private final byte msgType;

    private String api;

    private final String[] params;
}
