package com.zty.service;

import com.zty.spring.BeanPostProcessor;
import com.zty.spring.Component;

/**
 * @Author zty
 * @Date 2023/2/28 21:36
 */
@Component
public class ZtyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {
        if ("userService".equals(beanName)){
            System.out.println("这是在bean初始化之前执行");
        }
    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        if ("userService".equals(beanName)){
            System.out.println("这是在bean初始化之后执行");
        }
    }
}
