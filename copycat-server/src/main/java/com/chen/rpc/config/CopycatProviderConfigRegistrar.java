package com.chen.rpc.config;

import com.chen.rpc.registrar.RpcServerRegistrar;
import com.chen.rpc.util.PropertySourcesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class CopycatProviderConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopycatProviderConfigRegistrar.class);

    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Class<?>[] classes = CopycatProviderConfig.class.getDeclaredClasses();
        for (Class<?> clazz : classes) {
            Annotation zookeeperConfigurationAnnotation = clazz.getAnnotation(ProviderConfigurationBeanBinding.class);
            registerBeanDefinitions(zookeeperConfigurationAnnotation);
        }
        RpcServerRegistrar.registerRpcServerBeanDefinitions(beanDefinitionRegistry);
    }

    private void registerBeanDefinitions(Annotation annotation) {
        Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
        String prefix = (String) attributes.get("prefix");
        prefix = this.environment.resolvePlaceholders(prefix);
        Class<?> configClass = (Class<?>) attributes.get("type");
        Map<String, Object> subProperties = PropertySourcesUtils.getSubProperties(this.environment.getPropertySources(), this.environment, prefix);
        try {
            Object obj = configClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldValue = (String) subProperties.get(field.getName());
                if (fieldValue != null && !fieldValue.equals("")) {
                    field.set(obj, fieldValue);
                    LOGGER.info("配置->{}写入成功，值为->{}", prefix + field.getName(), fieldValue);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("创建配置文件->{}失败", configClass.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }
}
