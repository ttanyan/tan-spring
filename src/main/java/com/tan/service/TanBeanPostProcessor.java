package com.tan.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Tan Lianwang
 * @title: TanBeanPostProcessor
 * @date 2025/7/8 16:08
 */
@Component
public class TanBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {

        //扩展方法  属性注入
        for (Field declaredField : bean.getClass().getDeclaredFields()) {

            if(declaredField.isAnnotationPresent(TanValue.class)){
                //设置私有属性可访问
                declaredField.setAccessible(true);
                //获取注解
                String tanValue = declaredField.getAnnotation(TanValue.class).value();
                //给属性赋值
                declaredField.set(bean, tanValue);
            }
        }

        return bean;
    }






//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
//
//            if(beanName.equals("userService")){
//                Object instance = Proxy.newProxyInstance(TanBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//
//                        //切面的逻辑
//                        System.out.println("切面逻辑：方法 " + method.getName() + " 被调用");
//                        //执行被代理对象的逻辑
//                        return method.invoke(bean, args);
//                    }
//                });
//
//                return instance;
//
//            }
//
//
//        return bean;
//
//    }
}
