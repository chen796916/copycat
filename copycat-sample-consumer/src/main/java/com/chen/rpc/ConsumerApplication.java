package com.chen.rpc;

import com.chen.rpc.annotation.CopycatScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chen.rpc.controller", "com.chen.rpc.bean"})
@CopycatScan(basePackage = "com.chen.rpc.service")
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
