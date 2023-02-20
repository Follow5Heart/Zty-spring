package com.zty.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * @Author zty
 * @Date 2023/2/20 0:04
 */
public class ZtyApplicationContext {
    private Class className;

    public ZtyApplicationContext(Class className) {
        this.className = className;

        //当创建容器之后，就该进行扫描，通过什么扫描，那么信息从哪里来，从配置类来
        //判断当前这个配置类中是否 ComponentScan这个注解
        if (className.isAnnotationPresent(ComponentScan.class)) {
            //如果存这个注解，那么从这个配置 类获取这个ComponentScan这个注解对象
            ComponentScan componentScanAnnotation = (ComponentScan) className.getAnnotation(ComponentScan.class);

            //获取ComponentScan注解里面的属性  获取扫描路径
            String path = componentScanAnnotation.value(); // com.zty.service

            //对path进行处理，更新路径
            String dealPath = path.replace(".", "/");

            //通过ZtyApplicationContext的类对象的类加载器
            ClassLoader classLoader = ZtyApplicationContext.class.getClassLoader();
            //通过相对路径 来获取我们想要的绝对路径地址 D:/ywhz_project/ywhz-springcloud/Zty-spring/out/production/Zty-spring com.zty.service.Test
            URL resource = classLoader.getResource(dealPath);

            //通过获取的URL对象的getFile方法，来创建一个file对象，file对象可以是文件夹也可是文件
            File file = new File(resource.getFile());

            //如果是文件夹
            if (file.isDirectory()){
                //获取文件列表
                File[] files = file.listFiles();
                for (File f : files) {
                    String absolutePath = f.getAbsolutePath();


                    //判断那些文件是以.class结尾的
                    if (absolutePath.endsWith(".class")) {

                        //获取类加载器想要的路径 com/zty/service/Test.class
                        String classNamePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                        System.out.println(classNamePath);
                        classNamePath=classNamePath.replace("\\",".");

                        //用类加载器加载当前类
                        try {
                            Class<?> classObject = classLoader.loadClass(classNamePath);
                            if (classObject.isAnnotationPresent(Component.class)){
                                System.out.println("有这个注解");
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }




        }
    }

    public Object getBean(String BeanName) {
        return null;


    }
}
