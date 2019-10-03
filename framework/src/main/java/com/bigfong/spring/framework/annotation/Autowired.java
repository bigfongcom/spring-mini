package com.bigfong.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自动注入
 * @author bigfong
 * @since 2019/10/2
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
