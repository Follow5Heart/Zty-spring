package com.zty.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author zty
 * @Date 2023/2/20 0:06
 *
 * * @Retention用于指定Annotation可以保留多长时间。
 * @Retention时，必须为其value指定值。value成员变量的值只能是如下3个：
 * RetentionPolicy.SOURCE：Annotation只保留在源代码中，编译器编译时，直接丢弃这种Annotation。
 * RetentionPolicy.CLASS：编译器把Annotation记录在class文件中。当运行Java程序时，JVM中不再保留该Annotation。
 * RetentionPolicy.RUNTIME：编译器把Annotation记录在class文件中。当运行Java程序时，JVM会保留该Annotation，程序可以通过反射获取该Annotation的信息。* *
 * @Target指定Annotation用于修饰哪些程序元素。@Target也包含一个名为”value“的成员变量，该value成员变量类型为ElementType[ ]，ElementType为枚举类型，值有如下几个：
 * ElementType.TYPE：能修饰类、接口或枚举类型
 * ElementType.FIELD：能修饰成员变量
 * ElementType.METHOD：能修饰方法
 * ElementType.PARAMETER：能修饰参数
 * ElementType.CONSTRUCTOR：能修饰构造器
 * ElementType.LOCAL_VARIABLE：能修饰局部变量
 * ElementType.ANNOTATION_TYPE：能修饰注解
 * ElementType.PACKAGE：能修饰包*
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {
    String value() default "";
}
