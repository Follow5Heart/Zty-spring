package com.zty.service;

import com.zty.spring.BeanPostProcessor;
import com.zty.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author zty
 * @Date 2023/2/28 21:36
 */
@Component
public class ZtyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if ("userService".equals(beanName)){
            System.out.println("这是在bean初始化之前执行");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if ("userService".equals(beanName)){
            //System.out.println("这是在bean初始化之后执行");
            /*
            在Java中，Proxy.newProxyInstance是一个用于创建动态代理实例的静态方法。动态代理是一种通过代理对象来访问目标对象的技术，
            它可以在运行时动态地生成代理类，并将调用请求转发到目标对象，同时可以在转发请求前或之后添加一些自定义逻辑。
            该方法的第一个参数是一个类加载器对象，用于加载动态生成的代理类。第二个参数是一个Class数组，指定要代理的接口列表。
            第三个参数是一个InvocationHandler对象，用于处理代理类的方法调用请求。通过调用该方法，可以返回一个实现了指定接口列表的代理对象，
            该代理对象可以通过调用接口中定义的方法来访问目标对象的方法。在代理对象的方法调用过程中，会将请求转发到InvocationHandler对象中的invoke方法，
            以便在调用目标对象方法前或之后执行一些自定义逻辑。

            通过动态代理来创建一个实现了某个接口的代理对象，该代理对象会拦截目标对象的方法调用，并在方法调用前后添加自己的逻辑。动态代理常常被用于实现AOP（面向切面编程）。
            在这段代码中，使用了Java的Proxy类来创建一个代理对象，这个代理对象实现了ZtyBeanPostProcessor接口，用于在bean实例化之后和初始化之前添加额外的逻辑。
            在newProxyInstance方法的参数中，我们需要传递一个类加载器、一组接口以及一个实现了InvocationHandler接口的对象。其中，类加载器用于加载代理类，
            接口用于指定代理类要实现哪些接口，而InvocationHandler接口的实现则用于拦截方法调用并添加自己的逻辑。
            在invoke方法中，我们可以看到，当调用代理对象的方法时，首先会输出一行日志"开始切面逻辑"，然后调用bean的对应方法并返回结果。
            通过这种方式，我们可以在不修改原有代码的情况下，对某个对象的方法调用进行拦截并添加自己的逻辑，这就是Java动态代理的一个常见用法
            */

            Object proxyInstance=Proxy.newProxyInstance(ZtyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("开始切面逻辑");
                    return method.invoke(bean, args);
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}
