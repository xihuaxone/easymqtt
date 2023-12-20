package com.xihua.easyctl.controller;

import com.xihua.easyctl.annocation.MController;

@MController(topic = "/demo", qos = 1)
public class DemoController {
    public Object handle() {
        return "hello";
    }
}
