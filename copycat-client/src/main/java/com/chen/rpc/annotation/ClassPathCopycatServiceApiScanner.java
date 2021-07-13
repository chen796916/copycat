package com.chen.rpc.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassPathCopycatServiceApiScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathCopycatServiceApiScanner.class);

    private CopycatServiceFactoryBean<?> copycatServiceFactoryBean = new CopycatServiceFactoryBean<Object>();
    private Class<? extends Annotation> annotationClass;

    public ClassPathCopycatServiceApiScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            LOGGER.error("在->{}中找不到service的api", basePackages);
        } else {
            processBeanDefinition(beanDefinitionHolders);
        }
        return beanDefinitionHolders;
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader,
                                     MetadataReaderFactory metadataReaderFactory) {
                    return true;
                }
            });
        }

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader,
                                 MetadataReaderFactory metadataReaderFactory)
                    throws IOException {
                String className = metadataReader.getClassMetadata()
                        .getClassName();
                return className.endsWith("package-info");
            }
        });
    }

    private void processBeanDefinition(Set<BeanDefinitionHolder> beanDefinitionHolders) {
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(this.copycatServiceFactoryBean.getClass());
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
