package com.chen.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解@Reference
 * 通过将此注解放在消费者要注入bean的字段上，返回bean的代理对象
 * @author chen
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Autowired(required = false)
public @interface CopycatReference {
}
