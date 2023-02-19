package com.zty.service;

import com.zty.spring.ZtyApplicationContext;

/**
 * @Author zty
 * @Date 2023/2/19 23:58
 */
public class Test {
    public static void main(String[] args) {
        ZtyApplicationContext ztyApplicationContext = new ZtyApplicationContext(AppConfig.class);
        UserService userService = (UserService) ztyApplicationContext.getBean("userService");

    }
}
