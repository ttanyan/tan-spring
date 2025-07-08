package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tan Lianwang
 * @title: ComponetScan
 * @date 2025/7/8 09:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

    String value() default ""; // 用于指定扫描的包路径，默认为空字符串，表示不指定包路径


}
