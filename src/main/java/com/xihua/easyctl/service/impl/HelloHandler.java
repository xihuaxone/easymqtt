package com.xihua.easyctl.service.impl;

import com.xihua.easyctl.annocation.MService;
import com.xihua.easyctl.service.HandlerInterface;

@MService(api = "/hello")
public class HelloHandler implements HandlerInterface {
    @Override
    public String[] handle(String[] params) {
        return new String[] {"hello too"};
    }
}
