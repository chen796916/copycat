package com.chen.rpc.annotation;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

public class CopycatServiceFactoryBean<T> implements FactoryBean<T> {

    @Autowired
    ProxyFactory proxyFactory;

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
