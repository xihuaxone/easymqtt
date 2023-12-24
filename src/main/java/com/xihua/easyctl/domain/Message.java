package com.xihua.easyctl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@org.msgpack.annotation.Message
public class Message {
    private String reqId;

    private String sourceTopic;

    private String targetTopic;

    private String msgType;

    private String api;

    private List<String> params;
}
