package com.zty.spring;

/**
 * @Author zty
 * @Date 2023/2/28 20:53
 */
public interface BeanNameAware {
    /**
     * 给bean设置名字
     * @param beanName
     */
    void setBeanName(String beanName);
}
