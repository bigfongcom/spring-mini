package com.bigfong.spring.framework.webmvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 保存URL和Method的对应关系
 * 使用策略模式
 * @author bigfong
 * @since 2019/10/3
 */
public class HandlerMapping {
    private Object controller;//目标方法所在的controller对象
    private Method method;//URL对应的目标方法
    private Pattern pattern;//URL的封装

    public HandlerMapping(Pattern pattern,Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Annotation[][] getParameterAnnotations() {
        if (null==method){
            return null;
        }
        return method.getParameterAnnotations();
    }
}
