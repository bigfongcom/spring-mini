package com.bigfong.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 页面交互
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
