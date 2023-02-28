package com.zty.service;

import com.zty.spring.Autowired;
import com.zty.spring.BeanNameAware;
import com.zty.spring.Component;
import com.zty.spring.Scope;

/**
 * @Author zty
 * @Date 2023/2/19 23:58
 */
@Component()
@Scope("prototype")
public class UserService implements BeanNameAware {
    @Autowired
    private OrderService orderService;

    private String beanName;



    public void test(){
        System.out.println(orderService);

    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName=beanName;
    }
}
