package com.chen.rpc.controller;

import com.chen.rpc.serviceApi.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/simple")
public class SimpleController {

    @Autowired
    SimpleService simpleService;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public int doSomething(
            @RequestParam(value = "num") int num
    ){
        return simpleService.doSomething(num);
    }
}
