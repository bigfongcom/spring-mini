package com.bigfong.spring.framework.aop.aspect;

import com.bigfong.spring.framework.aop.intercept.MethodInterceptor;
import com.bigfong.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 异常通知
 * @author bigfong
 * @since 2019/10/4
 */
public class AfterThrowingAdvice extends AbstractAspectJAdivce implements Advice,MethodInterceptor{
    private String throwingName;
    private MethodInvocation mi;

    public AfterThrowingAdvice(Method aspectMethod, Object target) {
        super(aspectMethod,target);
    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        }catch (Throwable e){
            invokeAdviceMethod(mi,null,e.getCause());
            throw e;
        }
    }
}
