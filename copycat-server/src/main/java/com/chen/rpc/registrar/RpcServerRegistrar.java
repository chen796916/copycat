package com.chen.rpc.registrar;

import com.chen.rpc.config.ProviderRpcConfig;
import com.chen.rpc.config.ProviderZookeeperConfig;
import com.chen.rpc.nettyServer.RpcServer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class RpcServerRegistrar {

    public static void registerRpcServerBeanDefinitions( BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcServer.class);
        BeanDefinition definition = builder.getBeanDefinition();
        definition.getConstructorArgumentValues().addIndexedArgumentValue(0, ProviderRpcConfig.address);
        definition.getConstructorArgumentValues().addIndexedArgumentValue(1, ProviderZookeeperConfig.address);
        beanDefinitionRegistry.registerBeanDefinition("rpcServer", definition);
    }
}
