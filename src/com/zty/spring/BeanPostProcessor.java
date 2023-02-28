package com.zty.spring;

/**
 * @Author zty
 * @Date 2023/2/28 21:33
 */
public interface BeanPostProcessor {
    /**
     * 在bean初始化之前调用
     * @param beanName
     * @param bean
     */
    Object postProcessBeforeInitialization(String beanName,Object bean);

    /**
     * 在bean初始化之后调用
     * @param beanName
     * @param bean
     */
    Object postProcessAfterInitialization(String beanName,Object bean);
}
