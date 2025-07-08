package com.spring;

import com.sun.istack.internal.Nullable;

/**
 * @author Tan Lianwang
 * @title: BeanPostProcessor
 * @date 2025/7/8 16:06
 */
public interface BeanPostProcessor {

    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {

        return bean;
    }
}
