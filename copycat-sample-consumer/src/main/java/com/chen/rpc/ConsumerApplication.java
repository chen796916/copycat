package com.chen.rpc;

import com.chen.rpc.annotation.CopycatScan;
import com.chen.rpc.config.CopycatConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chen.rpc.controller", "com.chen.rpc.bean"})
@CopycatScan(basePackage = "com.chen.rpc.service")
@CopycatConsumer
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
