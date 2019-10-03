package com.bigfong.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 业务逻辑，注入接口
 * @author bigfong
 * @since 2019-10-02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
