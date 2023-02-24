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
        if (className==null){
            throw new NullPointerException("类对象不能为空");
        }
        //设置一个成员属性，在创建容器时候，传一个配置类，用于获取要加载配置类
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

            //获取当前线程中ZtyApplicationContext类的类加载器（类加载器是java中用于加载类和资源的抽象类）
            ClassLoader classLoader = ZtyApplicationContext.class.getClassLoader();
            //通过类加载器中getResource方法获取指定路径的资源，并返回一个url对象表示该资源的位置 com.zty.serviceCopy
            //通过相对路径 来获取我们想要的绝对路径地址 D:/ywhz_project/ywhz-springcloud/Zty-spring/out/production/Zty-spring com.zty.service.Test
            URL resource = classLoader.getResource(dealPath);

            //通过获取的URL对象的getFile方法就是路径，来创建一个file对象，file对象可以是文件夹也可是文件
            File file = new File(resource.getFile());

            try{
                //递归判断文件夹和文件，然后执行不同的操作
                traverseFolder(file);
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    public Object getBean(String BeanName) {
        return null;
    }


    /**
     * 递归遍历文件夹
     * @param file
     */
    private static void traverseFolder(File file) throws ClassNotFoundException {
        //如果是文件夹
        if (file.isDirectory()){
            //获取文件夹里面的文件列表
            File[] listFiles = file.listFiles();
            //遍历文件列表
            for (File listFile : listFiles) {
                if (listFile.isDirectory()){
                    traverseFolder(listFile);
                }else{
                    checkFileAndLoading(listFile);
                }

            }


        }else{
            //否则就是文件
            checkFileAndLoading(file);
        }
    }

    /**
     * 如果是文件，则进行判断 是否包含Component注解
     * @param listFile
     * @throws ClassNotFoundException
     */
    public static void checkFileAndLoading(File listFile) throws ClassNotFoundException {
        //判断是否是.class文件 为后缀的
        String fileName = listFile.getName();
        if (fileName.endsWith(".class")){

            //首先获取文件的绝对路径
            String absolutePath = listFile.getAbsolutePath();
            String className=absolutePath.substring(absolutePath.indexOf("com"),absolutePath.indexOf(".class"));
            className = className.replace("\\", ".");
            System.out.println("正在搜索："+className+"的类");
            Class<?> aClass = Class.forName(className);
            if (aClass.isAnnotationPresent(Component.class)) {
                Component componentAnnotation = aClass.getAnnotation(Component.class);
                String value = componentAnnotation.value();
                System.out.println(value+":含有Component注解");

            }

        }
    }

}
