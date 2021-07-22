package com.chen.rpc.config;

public class CopycatConsumerConfig {
    @ConsumerConfigurationBeanBinding(prefix = "copycat.zookeeper.", type = ConsumerZookeeperConfig.class)
    static class ZookeeperConfiguration{

    }
}
