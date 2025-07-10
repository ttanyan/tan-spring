package com.tan;

import com.spring.TanApplicationContext;
import com.tan.service.UserInterface;
import com.tan.service.UserService;

/**
 * @description:Test
 * @author: tanlianwang
 * @create: 2025−07-07 17:22
 **/
public class Test {


    public static void main(String[] args) {



        TanApplicationContext tanApplicationContext = new TanApplicationContext(AppConfig.class);
        UserService userService = (UserService) tanApplicationContext.getBean("userService");

        userService.test();

    }

}
