package com.chen.rpc.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);

    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> EXTENSION_INSTANCE = new ConcurrentHashMap<>();
    private static final Map<String, Object> cachedInstance = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();

    private static final String EXTENSION_DIRECTORY = "META-INF/copycat/";

    private Class<?> type;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension Type为空");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException(type + "不是一个接口");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException(type + "没有@SPI注解");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    public T getExtension(String name) {
        if (StringUtils.isEmpty(name)) {
            name = type.getAnnotation(SPI.class).value();
            if (StringUtils.isEmpty(name)) {
                throw new IllegalArgumentException("未配置" + type + "默认拓展类");
            }
        }
        Object instance = cachedInstance.get(name);
        if (instance == null) {
            synchronized (cachedInstance) {
                instance = cachedInstance.get(name);
                if (instance == null) {
                    instance = createExtension(name);
                    cachedInstance.put(name, instance);
                }
            }
        }
        return (T) instance;
    }

    private T createExtension(String name) {
        if (cachedClasses.isEmpty()) {
            loadExtensionClasses();
        }
        Class<?> clazz = cachedClasses.get(name);
        if (clazz == null) {
            throw new RuntimeException("找不到" + name + "的类");
        }
        Object instance = EXTENSION_INSTANCE.get(clazz);
        if (instance == null) {
            try {
                EXTENSION_INSTANCE.putIfAbsent(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (Throwable t) {
                throw new RuntimeException("实例化" + clazz + "时出错");
            }
            instance = EXTENSION_INSTANCE.get(clazz);
        }
        return (T) instance;
    }

    private void loadExtensionClasses() {
        loadDirectory(type.getName());
    }

    private void loadDirectory(String type) {
        String fileName = EXTENSION_DIRECTORY + type;
        try {
            Enumeration<URL> urls = null;
            ClassLoader cl = getClassLoader();
            urls = cl.getResources(fileName);
            if (!urls.hasMoreElements()) {
                urls = ClassLoader.getSystemResources(fileName);
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                loadResource(url, cl);
            }
        } catch (IOException e) {
            LOGGER.error("无法加载{}", fileName);
        }
    }

    private void loadResource(URL url, ClassLoader classLoader) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    String name = null;
                    String clazz = null;
                    int i = line.indexOf('=');
                    name = line.substring(0, i);
                    clazz = line.substring(i + 1);
                    loadClass(url, Class.forName(clazz, true, classLoader), name);
                }
            }
        } catch (Throwable t) {
            LOGGER.error("初始化{}时出错", type);
        }
    }

    private void loadClass(URL url, Class<?> clazz, String name) {
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException(clazz + "不是" + type + "的实现类");
        }
        try {
            clazz.getConstructor();
            cachedClasses.put(name, clazz);
        } catch (NoSuchMethodException e) {
            LOGGER.error("{}没有构造函数，无法初始化", clazz);
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ExtensionLoader.class.getClassLoader();
            if (cl == null) {
                cl = ClassLoader.getSystemClassLoader();
            }
        }
        return cl;
    }
}
