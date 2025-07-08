package com.tan.service;

import com.spring.Component;
import com.spring.Scope;

/**
 * @description:UserService
 * @author: tanlianwang
 * @create: 2025−07-07 17:23
 **/
@Component
@Scope("singleton") // 使用自定义的Scope注解，指定为单例模式
public class OrderService {

    public void test(){
        System.out.println("test");
    }
}
