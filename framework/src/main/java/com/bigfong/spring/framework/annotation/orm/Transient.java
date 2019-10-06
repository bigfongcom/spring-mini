package com.bigfong.spring.framework.annotation.orm;

import java.lang.annotation.*;

/**
 * 不序列化
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transient {
    String value() default "";
}
