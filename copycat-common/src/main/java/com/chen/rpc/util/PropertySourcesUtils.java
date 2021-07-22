package com.chen.rpc.util;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertySourcesUtils {

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

}
