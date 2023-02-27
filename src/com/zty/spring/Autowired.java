package com.zty.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author zty
 * @Date 2023/2/28 0:00
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
}
