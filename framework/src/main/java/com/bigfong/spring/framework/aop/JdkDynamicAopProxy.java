package com.bigfong.spring.framework.aop;

import com.bigfong.spring.framework.aop.intercept.MethodInvocation;
import com.bigfong.spring.framework.aop.support.AdvisedSupport;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 使用JDK动态代理生成代理类
 * 调用AdvisedSupport的getInterceptorsAndDynamicInterceptionAdvice方法获得拦截器链
 * 在目标类中，每一个被增强的目标方法都对应一个拦截链
 * @author bigfong
 * @since 2019/10/4
 */
public class JdkDynamicAopProxy implements AopProxy,InvocationHandler {
    private AdvisedSupport config;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    /**
     * 把原生的对象传进来
     * @return
     */
    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.config.getTargetClass().getInterfaces(),this);
    }

    /**
     * 执行代理的关键入口
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //将每一个JoinPoint也就是被代理的业务方法(Method)堀井封装成一个拦截器，组合成一个拦截器链
        List<Object> interceptorsAndDynamicMethodMatchers = config.getInterceptorsAndDynamicInterceptionAdvice(method,this.config.getTargetClass());
        //交给拦截器链MethodInvocation的proceed()方法执行
        MethodInvocation invocation = new MethodInvocation(proxy,this.config.getTarget(),method,args,this.config.getTargetClass(),interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
