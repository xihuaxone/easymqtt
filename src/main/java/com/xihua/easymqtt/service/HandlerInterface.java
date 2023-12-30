package com.xihua.easymqtt.service;

import java.util.List;

public interface HandlerInterface {
    List<String> handle(List<String> params);
}
