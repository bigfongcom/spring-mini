package com.bigfong.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Mapper
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
    String value() default "";
}
