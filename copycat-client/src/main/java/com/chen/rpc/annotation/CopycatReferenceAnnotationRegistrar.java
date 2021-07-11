package com.chen.rpc.annotation;

import com.chen.rpc.util.BeanRegistrar;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class CopycatReferenceAnnotationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        registerCopycatReferenceAnnotationBeanPostProcessor(beanDefinitionRegistry);
        System.out.println("211221212");
    }

    private void registerCopycatReferenceAnnotationBeanPostProcessor(BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanRegistrar.registerInfrastructureBean(beanDefinitionRegistry,
                CopycatReferenceAnnotationBeanPostProcessor.class.getName(),
                CopycatReferenceAnnotationBeanPostProcessor.class);
    }
}
