package com.chen.rpc.config;

@ConsumerConfigurationBeanBindings
public class CopycatConsumerConfig {
    @ConsumerConfigurationBeanBinding(prefix = "copycat.zookeeper.", type = ConsumerZookeeperConfig.class)
    static class ZookeeperConfiguration{

    }
}
