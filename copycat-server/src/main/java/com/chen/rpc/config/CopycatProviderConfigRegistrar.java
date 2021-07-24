package com.chen.rpc.config;

import com.chen.rpc.registrar.RpcServerRegistrar;
import com.chen.rpc.util.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

public class CopycatProviderConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopycatProviderConfigRegistrar.class);

    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Class<?>[] classes = CopycatProviderConfig.class.getDeclaredClasses();
        for (Class<?> clazz : classes) {
            Annotation zookeeperConfigurationAnnotation = clazz.getAnnotation(ProviderConfigurationBeanBinding.class);
            ConfigurationUtils.registerBeanDefinitions(zookeeperConfigurationAnnotation, this.environment);
        }
        RpcServerRegistrar.registerRpcServerBeanDefinitions(beanDefinitionRegistry);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }
}
