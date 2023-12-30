package com.xihua.easymqtt.common;

import com.xihua.easymqtt.domain.Message;

import java.util.concurrent.FutureTask;

public class ReqFuture extends FutureTask<Message> {
    private final long expireAt;

    public ReqFuture() {
        this(1000 * 10);
    }

    public ReqFuture(int timeout) {
        super(() -> null);
        expireAt = System.currentTimeMillis() + timeout;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }

    public void setResp(Message resp) {
        super.set(resp);
    }
}
