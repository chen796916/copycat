package com.chen.rpc;

import com.chen.rpc.config.CopycatProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chen.rpc.controller","com.chen.rpc.service"})
@CopycatProvider
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
