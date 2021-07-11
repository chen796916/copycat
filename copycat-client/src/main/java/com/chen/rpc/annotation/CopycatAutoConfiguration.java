package com.chen.rpc.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CopycatAutoConfiguration {

    @Bean
    public CopycatReferenceAnnotationBeanPostProcessor setCopycatReferenceAnnotationBeanPostProcessor() {
        return new CopycatReferenceAnnotationBeanPostProcessor();
    }
}
