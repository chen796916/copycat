package com.chen.rpc.config;

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

public class CopycatConsumerConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopycatConsumerConfigRegistrar.class);

    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Class<?>[] classes = CopycatConsumerConfig.class.getDeclaredClasses();
        for (Class<?> clazz : classes) {
            Annotation zookeeperConfigurationAnnotation = clazz.getAnnotation(ConsumerConfigurationBeanBinding.class);
            ConfigurationUtils.registerBeanDefinitions(zookeeperConfigurationAnnotation, this.environment);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }
}
