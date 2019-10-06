package com.bigfong.spring.framework.annotation.orm;

import java.lang.annotation.*;

/**
 * 设置主键
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String value() default "";
}
