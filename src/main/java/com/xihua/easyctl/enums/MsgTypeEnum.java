package com.xihua.easyctl.enums;

public enum MsgTypeEnum {
    REQUEST((byte) 1),

    RESPONSE((byte) 2);

    private final byte msgType;

    MsgTypeEnum(byte msgType) {
        this.msgType = msgType;
    }

    public byte getMsgType() {
        return msgType;
    }
}
