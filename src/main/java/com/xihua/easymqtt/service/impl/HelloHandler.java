package com.xihua.easymqtt.service.impl;

import com.xihua.easymqtt.annocation.MService;
import com.xihua.easymqtt.service.HandlerInterface;

import java.util.Arrays;
import java.util.List;

@MService(api = "/hello")
public class HelloHandler implements HandlerInterface {
    @Override
    public List<Object> handle(List<Object> params) {
        return Arrays.asList("hello too");
    }
}
