package com.bigfong.spring.framework.aop.support;

import com.bigfong.spring.framework.aop.AopConfig;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 主要解析和封装AOP配置
 * 其中pointCutMatch()方法用来判断目标类是否符合切面规则，从而决定是否需要生成代理类，对目标方法进行增强
 * getInterceptorsAndDynamicInterceptionAdvice()方法主要根据AOP配置，将需要回调的方法封装成一个拦截链并返回提供给外部获取
 * @Author Bigfong
 * @Date 2019/10/4
 **/
public class AdvisedSupport {
    private Class targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;
    private AopConfig config;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?> targetClass) throws Exception{
        List<Object> cached = methodCache.get(method);

        if (null == cached){
            
        }
    }
}
