package com.bigfong.spring.framework.aop.aspect;

import com.bigfong.spring.framework.aop.intercept.MethodInterceptor;
import com.bigfong.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知实现
 * @author bigfong
 * @since 2019/10/4
 */
public class MethodBeforeAdvice extends AbstractAspectJAdivce implements Advice,MethodInterceptor {
    private JoinPoint joinPoint;

    public MethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method,Object[] args,Object target) throws Throwable{
        invokeAdviceMethod(this.joinPoint,null,null);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        this.before(mi.getMethod(),mi.getArguments(),mi.getThis());
        return mi.proceed();
    }
}
