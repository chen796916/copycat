package com.chen.rpc.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

    public static Map<String, Object> convertToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        Class<?> clazz = obj.getClass();
        return convertToMap(clazz);
    }

    public static Map<String, Object> convertToMap(Class<?> clazz) {
        Map<String, Object> map = new HashMap<>(clazz.getDeclaredFields().length);
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldVal = field.get(clazz);
                map.put(fieldName, fieldVal);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

}
