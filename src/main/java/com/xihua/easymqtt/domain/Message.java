package com.xihua.easymqtt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {
    private String reqId;

    private String sourceTopic;

    private String targetTopic;

    private String msgType;

    private String api;

    private List<Object> params;
}
