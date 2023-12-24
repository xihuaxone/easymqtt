package com.xihua.easyctl.enums;

public enum MsgTypeEnum {
    REQUEST("1"),

    RESPONSE("2");

    private final String msgType;

    MsgTypeEnum(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }
}
