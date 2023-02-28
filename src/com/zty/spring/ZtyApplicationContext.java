package com.zty.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zty
 * @Date 2023/2/20 0:04
 */
public class ZtyApplicationContext {

    /**
     * 保存类型
     */
    private Class className;

    /**
     * 用于保存要创建bean的 beanDefinition
     */
    private ConcurrentHashMap<String, BeanDefinition> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 单例池map,用于保存单例bean对象*
     */
    private ConcurrentHashMap<String, Object> singletonPoolMap = new ConcurrentHashMap<>();

    public ZtyApplicationContext(Class className) {
        if (className == null) {
            throw new NullPointerException("类对象不能为空");
        }
        //设置一个成员属性，在创建容器时候，传一个配置类，用于获取要加载配置类
        this.className = className;

        //当创建容器之后，就该进行扫描，通过什么扫描，那么信息从哪里来，从配置类来
        //判断当前这个配置类中是否 ComponentScan这个注解
        if (className.isAnnotationPresent(ComponentScan.class)) {
            //如果存这个注解，那么从这个配置 类获取这个ComponentScan这个注解对象
            ComponentScan componentScanAnnotation = (ComponentScan) className.getAnnotation(ComponentScan.class);

            //获取ComponentScan注解里面的属性  获取扫描路径 com.zty.service
            String path = componentScanAnnotation.value();

            //对path进行处理，更新路径
            String dealPath = path.replace(".", "/");

            //获取当前线程中ZtyApplicationContext类的类加载器（类加载器是java中用于加载类和资源的抽象类）
            ClassLoader classLoader = ZtyApplicationContext.class.getClassLoader();
            //通过类加载器中getResource方法获取指定路径的资源，并返回一个url对象表示该资源的位置 com.zty.serviceCopy
            //通过相对路径 来获取我们想要的绝对路径地址 D:/ywhz_project/ywhz-springcloud/Zty-spring/out/production/Zty-spring com.zty.service.Test
            URL resource = classLoader.getResource(dealPath);

            System.out.println("要搜索的资源范围："+resource.getFile());
            //通过获取的URL对象的getFile方法就是路径，来创建一个file对象，file对象可以是文件夹也可是文件
            File file = new File(resource.getFile());

            try {
                //递归判断文件夹和文件，然后执行不同的操作
                traverseFolder(file, concurrentHashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //过滤出concurrentHashMap中的单例bean，进行创建
        concurrentHashMap.entrySet().stream().filter(entry -> "singleton".equals(entry.getValue().getScope())
        ).forEach(entry -> {
            Object bean=createBean(entry.getKey(), entry.getValue());
            //把bean对象保存到单例池中
            singletonPoolMap.put(entry.getKey(), bean==null?"":bean);

        });
    }

    /**
     * 创建bean
     * @param beanName bean名字
     * @param beanDefinition bean的定义类
     * @return
     */
    private Object createBean(String beanName,BeanDefinition beanDefinition) {
        //只是简单写一下bean的创建
        //获取在beanDefinition中的类类型
        Class classType = beanDefinition.getType();
        //通过反射创建对象
        Object bean = null;
        try {
            bean = classType.getConstructor().newInstance();

            //TODO 依赖注入
            /*
            class.getDeclaredFields() 是 Java 反射 API 中的一个方法，它用于获取一个类中声明的所有字段（field），包括私有字段和继承自父类的字段。
            具体来说，它返回一个 Field 类型的数组，每个 Field 对象代表一个类中声明的字段。可以通过这些 Field 对象获取字段的名称、类型、修饰符等信息，
            并可以使用 Field 对象来读取或修改对象中的对应字段的值。
             */
            Field[] declaredFields = classType.getDeclaredFields();
            //遍历每个字段，判断是否包含Autowired注解
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    //把私有的也变成 可访问的
                    declaredField.setAccessible(true);
                    //修改一个对象中字段的值，可以通过 Field 对象的 set() 方法来实现。 set() 方法有两个参数，第一个参数是要修改值的对象，第二个参数是要设置的新值
                    declaredField.set(bean,getBean(declaredField.getName()));
                }

            }

            //TODO:Aware回调   逻辑就是当前的对象是否实现了Aware回调接口，如果实现了，调用重写的方法
            //判断当前类型是否实现了BeanNameAware
            if (bean instanceof BeanNameAware){
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return bean;

    }

    /**
     * 获取bean对象方法
     *
     * @param beanName bean的名字
     * @return bean对象
     */
    public Object getBean(String beanName) {
        Object bean=null;

        //通过beanName,获取concurrentHashMap中的beanDefinition
        BeanDefinition beanDefinition = concurrentHashMap.get(beanName);
        //如果beanDefinition，说明beanName不对或者在容器中不存在这个beanDefinition
        if (beanDefinition == null) {
            throw new NullPointerException("容器中不存在这个beanDefinition或beanName值错误");
        } else {
            //如果存在，就需要判断这个beanDefinition这个对象是单例还是多例
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                //单例的情况,那就是在程序启动的过程中，就创建出来，所以就需要在容器的构造方法中先进行创建
                //从单例池中获取bean对象
                bean = singletonPoolMap.get(beanName);
                //如果没有获取到单例bean，说明这个bean还没有在单例池中创建出来，那就创建出来，加入到单例池中
                if (bean == null){
                    bean = createBean(beanName, beanDefinition);
                    singletonPoolMap.put(beanName,bean);
                }

            } else if ("prototype".equals(scope)) {
                //多例的情况,不用考虑那么多，每次都是重新创建的
                bean = createBean(beanName, beanDefinition);
            } else {
                throw new RuntimeException("scope的填写的值错误");
            }
        }
        return bean;
    }


    /**
     * 递归遍历文件夹
     *
     * @param file
     */
    private static void traverseFolder(File file, ConcurrentHashMap<String, BeanDefinition> concurrentHashMap) throws ClassNotFoundException {
        //如果是文件夹
        if (file.isDirectory()) {
            //获取文件夹里面的文件列表
            File[] listFiles = file.listFiles();
            //遍历文件列表
            for (File listFile : listFiles) {
                if (listFile.isDirectory()) {
                    traverseFolder(listFile, concurrentHashMap);
                } else {
                    checkFileAndLoading(listFile, concurrentHashMap);
                }

            }


        } else {
            //否则就是文件
            checkFileAndLoading(file, concurrentHashMap);
        }
    }

    /**
     * 如果是文件，则进行判断 是否包含Component注解
     *
     * @param listFile
     * @throws ClassNotFoundException
     */
    public static void checkFileAndLoading(File listFile, ConcurrentHashMap<String, BeanDefinition> concurrentHashMap) throws ClassNotFoundException {
        //判断是否是.class文件 为后缀的
        String fileName = listFile.getName();
        if (fileName.endsWith(".class")) {

            //首先获取文件的绝对路径
            String absolutePath = listFile.getAbsolutePath();
            String className = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
            className = className.replace("\\", ".");
            System.out.println("正在搜索：" + className + "的类");
            Class<?> aClass = Class.forName(className);
            if (aClass.isAnnotationPresent(Component.class)) {
                //获取Component注解对象，获取注解中的值，就是bean的名称，如果没有，应该就是他的类名
                Component componentAnnotation = aClass.getAnnotation(Component.class);
                //获取Component注解里面的设置bean的值，就是bean的名字，如果没有填写，默认情况下该注解的值是当前类名的首字母小写形式。 MyComponent--myComponent
                String beanName = componentAnnotation.value();
                //如果等于"" 获取类名，首字母小写
                if ("".equals(beanName)) {
                    String aClassName = aClass.getSimpleName();
                    beanName = Character.toLowerCase(aClassName.charAt(0)) + aClassName.substring(1);
                }
                System.out.println(beanName + ":含有Component注解");

                //如果搜索到了含有Component注解，说明这里定义了一个bean 所以我们要生成一个beanDefinition对象
                BeanDefinition beanDefinition = new BeanDefinition();
                //设置beanDefinition的类型是什么
                beanDefinition.setType(aClass);
                //设置bean的作用域是什么,前提是需要判断当前类有没有@Scope注解，如果没有默认为单例，如果有获取类型

                if (aClass.isAnnotationPresent(Scope.class)) {
                    //如果有，获取里面作用域值
                    Scope scopeAnnotation = aClass.getAnnotation(Scope.class);
                    String scopeType = scopeAnnotation.value();
                    beanDefinition.setScope(scopeType);
                } else {
                    //如果没有Scope注解默认为单例bean
                    beanDefinition.setScope("singleton");

                }

                //最后存入到concurrentHashMap中
                concurrentHashMap.put(beanName, beanDefinition);

            }

        }
    }

}
