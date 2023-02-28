package com.zty.spring;

/**
 * @Author zty
 * @Date 2023/2/28 21:04
 */
public interface InitializingBean {
    /**
     * 初始化方法
     */
    void afterPropertiesSet();
}
