package com.bigfong.spring.framework.aop.aspect;

import com.bigfong.spring.framework.aop.intercept.MethodInterceptor;
import com.bigfong.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置通知
 * @author bigfong
 * @since 2019/10/4
 */
public class AfterReturningAdvice extends AbstractAspectJAdivce implements Advice,MethodInterceptor {
    private JoinPoint joinPoint;

    public AfterReturningAdvice(Method aspectMeth,Object target) {
        super(aspectMeth,target);
    }

    public void afterReturning(Object returnValue,Method method,Object[] args,Object target) throws Throwable{
        invokeAdviceMethod(joinPoint,returnValue,null);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal,mi.getMethod(),mi.getArguments(),mi.getThis());
        return retVal;
    }
}
