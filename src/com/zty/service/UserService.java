package com.zty.service;

import com.zty.spring.Autowired;
import com.zty.spring.Component;
import com.zty.spring.Scope;

/**
 * @Author zty
 * @Date 2023/2/19 23:58
 */
@Component()
@Scope("prototype")
public class UserService {
    @Autowired
    private OrderService orderService;

    public void test(){
        System.out.println(orderService);

    }
}
