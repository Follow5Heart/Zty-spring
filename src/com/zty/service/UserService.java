package com.zty.service;

import com.zty.spring.*;

/**
 * @Author zty
 * @Date 2023/2/19 23:58
 */
@Component()
@Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean {
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

    @Override
    public void afterPropertiesSet() {
        //在初始化的时候调用，也是判断当前类中是否包实现了Initializing类型
        System.out.println("afterPropertiesSet方法被调用了，当前类实现了Initializing");

    }
}
