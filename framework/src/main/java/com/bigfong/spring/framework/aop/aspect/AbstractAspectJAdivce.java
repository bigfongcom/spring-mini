package com.bigfong.spring.framework.aop.aspect;

import com.bigfong.spring.framework.aop.intercept.MethodInterceptor;

import java.lang.reflect.Method;

/**
 * 封装拦截器回调的通用逻辑
 * 主要封装了反射动态调用方法
 * @author bigfong
 * @since 2019/10/4
 */
public abstract class AbstractAspectJAdivce implements Advice{
    private Method aspectMethod;
    private Object aspectTarget;

    public AbstractAspectJAdivce(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    //反射动态调用方法
    protected Object invokeAdviceMethod(JoinPoint joinPoint,Object returnValue, Throwable ex) throws Throwable{
        Class<?>[] paramTypes = this.aspectMethod.getParameterTypes();
        if (null == paramTypes || paramTypes.length==0){
            return this.aspectMethod.invoke(aspectTarget);
        }else{
            Object[] args = new Object[paramTypes.length];
            for (int i=0;i<paramTypes.length;i++){
                if (paramTypes[i]==JoinPoint.class){
                    args[i] = joinPoint;
                }else if(paramTypes[i] == Throwable.class){
                    args[i] = ex;
                }else if(paramTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget,args);
        }
    }
}
