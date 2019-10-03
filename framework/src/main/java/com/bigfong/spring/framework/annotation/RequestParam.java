package com.bigfong.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 请求参数映射
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
