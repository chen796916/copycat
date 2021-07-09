package com.chen.rpc.service.impl;

import com.chen.rpc.ChenRpcService;
import com.chen.rpc.serviceApi.SimpleService;

@ChenRpcService
public class SimpleServiceImpl implements SimpleService {
    public int doSomething(int num) {
        System.out.println(num);
        return num+1;
    }
}
