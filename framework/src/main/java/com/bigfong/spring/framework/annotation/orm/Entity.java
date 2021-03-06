package com.bigfong.spring.framework.annotation.orm;

import java.lang.annotation.*;

/**
 * 实体类
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entity {
    String value() default "";
}
