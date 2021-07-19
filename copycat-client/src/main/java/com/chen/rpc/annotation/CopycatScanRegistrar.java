package com.chen.rpc.annotation;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class CopycatScanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        System.out.println(annotationMetadata);
        AnnotationAttributes copycatScanAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(CopycatScan.class.getName()));
        String[] basePackage = (String[]) copycatScanAttributes.get("basePackage");
        List<String> basePackageList = Arrays.asList(basePackage);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CopycatScannerConfigurer.class);
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackageList));
        beanDefinitionRegistry.registerBeanDefinition(annotationMetadata.getClassName() + "#" + CopycatScanRegistrar.class.getName() + 0, builder.getBeanDefinition());
    }

}
