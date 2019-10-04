package com.bigfong.spring.framework.aop.intercept;

/**
 * 方法拦截器顶层接口
 * AOP代码增强的基础组成单元
 * 主要子类:MethodBeforeAdvice、AfterReturnAdvice、AfterThrowingAdvice
 */
public interface MethodInterceptor {
    Object invoke(MethodInvocation mi) throws Throwable;
}
