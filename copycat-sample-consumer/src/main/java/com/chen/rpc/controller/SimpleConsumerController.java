package com.chen.rpc.controller;

import com.chen.rpc.service.SimpleService;
import com.chen.rpc.service.SimpleService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/consumer/simple")
public class SimpleConsumerController {

    @Autowired
    SimpleService simpleService;
    @Autowired
    SimpleService1 simpleService1;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public int doSomething(
            @RequestParam("num")int num
    ){
        return simpleService.doSomething(num);
        // RpcProxy rpcProxy = new RpcProxy(zookeeperAddress);
        // SimpleService simpleService = rpcProxy.create(SimpleService.class);
        // return simpleService.doSomething(num);
    }

    @RequestMapping(value = "/get1",method = RequestMethod.GET)
    public int doSomething2(
            @RequestParam("num")int num
    ){
        return simpleService1.doSomething2(num);
    }
}
