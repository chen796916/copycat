package com.chen.rpc.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class EnableProviderConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CopycatProviderConfig.class);
        beanDefinitionRegistry.registerBeanDefinition("CopycatProviderConfig", builder.getBeanDefinition());
    }
}
