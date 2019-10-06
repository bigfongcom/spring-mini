package com.bigfong.spring.framework.annotation.orm;

import java.lang.annotation.*;

/**
 * 关联表名
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value() default "";
}
