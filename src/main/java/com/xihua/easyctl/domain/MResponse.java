package com.xihua.easyctl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MResponse {

    private int reqId;
    private String sourceTopic;

    private String targetTopic;

    private byte msgType;

    private String api;

    private String[] params;
}
