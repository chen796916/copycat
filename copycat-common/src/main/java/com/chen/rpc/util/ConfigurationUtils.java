package com.chen.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtils.class);

    public static Map<String, Object> getSubProperties(PropertySources propertySources, PropertyResolver propertyResolver, String prefix) {
        Map<String, Object> subProperties = new LinkedHashMap();
        Iterator iterator = propertySources.iterator();
        while(true) {
            PropertySource source;
            do {
                if (!iterator.hasNext()) {
                    return Collections.unmodifiableMap(subProperties);
                }

                source = (PropertySource)iterator.next();
            } while(!(source instanceof EnumerablePropertySource));

            String[] var7 = ((EnumerablePropertySource)source).getPropertyNames();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String name = var7[var9];
                if (!subProperties.containsKey(name) && name.startsWith(prefix)) {
                    String subName = name.substring(prefix.length());
                    if (!subProperties.containsKey(subName)) {
                        Object value = source.getProperty(name);
                        if (value instanceof String) {
                            value = propertyResolver.resolvePlaceholders((String)value);
                        }

                        subProperties.put(subName, value);
                    }
                }
            }
        }
    }

    public static void registerBeanDefinitions(Annotation annotation, ConfigurableEnvironment environment) {
        Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
        String prefix = (String) attributes.get("prefix");
        prefix = environment.resolvePlaceholders(prefix);
        Class<?> configClass = (Class<?>) attributes.get("type");
        Map<String, Object> subProperties = ConfigurationUtils.getSubProperties(environment.getPropertySources(), environment, prefix);
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
}
