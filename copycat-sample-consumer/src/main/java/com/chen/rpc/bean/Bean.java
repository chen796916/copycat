package com.chen.rpc.bean;

import com.chen.rpc.annotation.ProxyFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bean {

    @org.springframework.context.annotation.Bean
    public ProxyFactory setProxyFactory() {
        return new ProxyFactory("172.17.0.2:2181");
    }
}
