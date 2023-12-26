package com.xihua.easyctl;

import com.xihua.easyctl.common.ReqFuture;
import com.xihua.easyctl.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class RequestManager {
    private static final ConcurrentHashMap<String, ReqFuture> REQ_MAP = new ConcurrentHashMap<>(100);

    private static final Logger logger = LoggerFactory.getLogger(RequestManager.class);

    private void cleanExpired() {
        REQ_MAP.forEach((reqId, reqFuture) -> {
            if (reqFuture.isExpired()) {
                REQ_MAP.remove(reqId);
                logger.info("reqId = " + reqId + " has been expired. remove");
            }
        });
    }

    public void add(String reqId) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        REQ_MAP.put(reqId, new ReqFuture());
    }

    public ReqFuture get(String reqId) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        return REQ_MAP.getOrDefault(reqId, null);
    }

    public ReqFuture remove(String reqId) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        return REQ_MAP.remove(reqId);
    }

    public void updateResult(String reqId, Message message) {
        if (REQ_MAP.size() > 500) {
            cleanExpired();
        }
        ReqFuture reqFuture = REQ_MAP.getOrDefault(reqId, null);
        if (reqFuture != null) {
            reqFuture.setResp(message);
        }
    }

}
