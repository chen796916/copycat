package com.chen.rpc.annotation;

import com.chen.rpc.config.ConsumerZookeeperConfig;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class CopycatServiceFactoryBean<T> implements FactoryBean<T> {

    private ProxyFactory proxyFactory = new ProxyFactory(ConsumerZookeeperConfig.address);

    private Class<T> serviceApiInterface;

    public CopycatServiceFactoryBean() {}

    public CopycatServiceFactoryBean(Class<T> serviceApiInterface) {
        this.serviceApiInterface = serviceApiInterface;
    }

    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(serviceApiInterface.getClassLoader(), new Class[] { serviceApiInterface }, proxyFactory);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceApiInterface;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
