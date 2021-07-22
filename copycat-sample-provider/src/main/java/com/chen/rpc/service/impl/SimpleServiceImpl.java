package com.chen.rpc.service.impl;

import com.chen.rpc.annotation.CopycatService;
import com.chen.rpc.service.SimpleService;

@CopycatService
public class SimpleServiceImpl implements SimpleService {
    public int doSomething(int num) {
        System.out.println(num);
        return num+1;
    }
}
