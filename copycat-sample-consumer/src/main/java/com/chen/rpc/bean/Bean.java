package com.chen.rpc.bean;

import com.chen.rpc.annotation.CopycatReferenceAnnotationBeanPostProcessor;
import com.chen.rpc.annotation.CopycatReferenceAnnotationRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CopycatReferenceAnnotationRegistrar.class)
public class Bean {

    @org.springframework.context.annotation.Bean
    public CopycatReferenceAnnotationBeanPostProcessor setCopycatReferenceAnnotationBeanPostProcessor() {
        return new CopycatReferenceAnnotationBeanPostProcessor();
    }
}
