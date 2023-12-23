package com.xihua.easyctl;

import com.xihua.easyctl.common.ReqFuture;
import com.xihua.easyctl.domain.Message;

import java.util.concurrent.ConcurrentHashMap;

public class RequestManager {
    private static final ConcurrentHashMap<Integer, ReqFuture> REQ_MAP = new ConcurrentHashMap<>(100);

    private void cleanExpired() {
        REQ_MAP.forEach((reqId, reqFuture) -> {
            if (reqFuture.isExpired()) {
                REQ_MAP.remove(reqId);
                System.out.println("reqId = " + reqId + " has been expired. remove");
            }
        });
    }

    public void add(Integer reqId) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        REQ_MAP.put(reqId, new ReqFuture());
    }

    public ReqFuture get(Integer reqId) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        return REQ_MAP.getOrDefault(reqId, null);
    }

    public ReqFuture remove(Integer reqId) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        return REQ_MAP.remove(reqId);
    }

    public void updateResult(Integer reqId, Message message) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        ReqFuture reqFuture = REQ_MAP.getOrDefault(reqId, null);
        if (reqFuture != null) {
            reqFuture.setResp(message);
        }
    }

}
