package com.xihua.easyctl.service.impl;

import com.xihua.easyctl.annocation.MService;
import com.xihua.easyctl.service.HandlerInterface;

import java.util.Arrays;
import java.util.List;

@MService(api = "/hello")
public class HelloHandler implements HandlerInterface {
    @Override
    public List<String> handle(List<String> params) {
        return Arrays.asList("hello too");
    }
}
