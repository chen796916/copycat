package com.chen.rpc.config;

@ProviderConfigurationBeanBindings
public class CopycatProviderConfig {
    @ProviderConfigurationBeanBinding(prefix = "copycat.zookeeper.", type = ProviderZookeeperConfig.class)
    static class ZookeeperConfiguration{

    }

    @ProviderConfigurationBeanBinding(prefix = "copycat.provider.", type = ProviderRpcConfig.class)
    static class RpcConfiguration{

    }
}
