package com.bigfong.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 请求URL
 * @author bigfong
 * @since 2019/10/2
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
