package com.zty.service;

import com.zty.spring.ZtyApplicationContext;

/**
 * @Author zty
 * @Date 2023/2/19 23:58
 */
public class Test {
    public static void main(String[] args) {
        ZtyApplicationContext ztyApplicationContext = new ZtyApplicationContext(AppConfig.class);
        UserInterface userService = (UserInterface) ztyApplicationContext.getBean("userService");

//        System.out.println(ztyApplicationContext.getBean("userService"));
//        System.out.println(ztyApplicationContext.getBean("userService"));
//        System.out.println(ztyApplicationContext.getBean("userService"));
//        System.out.println(ztyApplicationContext.getBean("userService"));
//        System.out.println(ztyApplicationContext.getBean("userService"));

        userService.test1();

    }
}
