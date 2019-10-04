package com.bigfong.spring.framework.aop.intercept;

import com.bigfong.spring.framework.aop.aspect.JoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行拦截器链，相当于Spring中的ReflectiveMethodInvocation的功能
 * @author bigfong
 * @since 2019/10/4
 */
public class MethodInvocation implements JoinPoint {
    private Object proxy;//代理对象
    private Method method;//代理的目标方法
    private Object target;//代理的目标对象
    private Class<?> targetClass;//代理的目标类
    private Object[] arguments;//代理的方法的实参列表
    private List<Object> interceptorsAndDynamicMethodMatchers;//回调方法链
    private Map<String,Object> userAttribute;//保存自定义属性
    private int currentInterceptorIndex = -1;

    public MethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }


    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public void setUserAttributre(String key, Object value) {
        if (null != value){
            if (this.userAttribute == null){
                this.userAttribute = new HashMap<>();
            }
            this.userAttribute.put(key,value);
        }else{
            if (this.userAttribute != null){
                this.userAttribute.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.userAttribute!=null ? this.userAttribute.get(key):null);
    }

    public Object proceed() throws Throwable{
        //如果Interceptor执行完，则执行joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size()-1){
            return this.method.invoke(this.target,this.arguments);
        }

        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

        //如果要动态匹配joinPoint
        if (interceptorOrInterceptionAdvice instanceof  MethodInterceptor){
            MethodInterceptor mi = (MethodInterceptor) interceptorOrInterceptionAdvice;
            return  mi.invoke(this);
        }else{
            //执行当前Interceptor
            return proceed();
        }
    }
}
