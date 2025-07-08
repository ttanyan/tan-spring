package com.spring;

/**
 * @author Tan Lianwang
 * @title: InitializingBean
 * @date 2025/7/8 16:01
 */
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
