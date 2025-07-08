package com.spring;

/**
 * @description:BeanDefinition
 * @author: tanlianwang
 * @create: 2025−07-08 10:34
 **/
public class BeanDefinition {
    private Class type; // Bean的类型

    private String scope; // Bean的作用域，默认为单例

    private boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
