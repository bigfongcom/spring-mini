package com.bigfong.spring.framework.annotation.orm;

import java.lang.annotation.*;

/**
 * 列名
 *
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String name() default "";

    boolean insertable() default false;

    boolean updateble() default false;
}
