package com.xihua.easymqtt.service;

import java.util.List;

public interface HandlerInterface {
    List<Object> handle(List<Object> params);
}
