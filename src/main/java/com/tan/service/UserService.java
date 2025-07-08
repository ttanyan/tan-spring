package com.tan.service;

import com.spring.*;

/**
 * @description:UserService
 * @author: tanlianwang
 * @create: 2025−07-07 17:23
 **/

@Component
@Scope("property") // 使用自定义的Scope注解，指定为单例模式
public class UserService implements BeanNameAware {

    @TanValue("xxx")
    private String test;

    private String beanName;

    @Autowired
    private OrderService orderService;

    public void test(){
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("初始化");
//    }
}
