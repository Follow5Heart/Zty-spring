package com.zty.spring;

/**
 * @Author zty
 * @Date 2023/2/20 22:48
 * bean的定义*
 */
public class BeanDefinition {
    /**
     * bean的类型
     */
    private Class type;
    /**
     * bean的作用域  单例  多例
     */
    private String scope;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
