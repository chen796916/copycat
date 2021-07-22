package com.chen.rpc.annotation;

import com.chen.rpc.config.CopycatConsumerConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在consumer端扫描接口文件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({CopycatScanRegistrar.class, CopycatConsumerConfigRegistrar.class})
public @interface CopycatScan {

    /**
     * 接口所在文件
     * @return
     */
    String[] basePackage() default {};

}
