package com.bigfong.spring.demo.aspect;

import com.bigfong.spring.demo.service.impl.QueryService;
import com.bigfong.spring.framework.aop.aspect.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * AOP演示
 */
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(QueryService.class);

    /**
     * 在调用 一个方法之前，执行before()方法
     *
     * @param joinPoint
     */
    public void before(JoinPoint joinPoint) {
        joinPoint.setUserAttributre("startTime_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
        //这个方法中的逻辑是由我们自己写的
        logger.info("Invoker Before Method==>" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    /**
     * 在调用 一个方法之后，执行after()方法
     *
     * @param joinPoint
     */
    public void after(JoinPoint joinPoint) {
        logger.info("Invoker After Method==>" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time :" + (endTime - startTime));
    }

    /**
     * 在调用 一个方法之后，执行after()方法
     *
     * @param joinPoint
     */
    public void afterThrowing(JoinPoint joinPoint,Throwable ex) {
        logger.info("出现异常:" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments())+
        "\nThrows:"+ex.getMessage());

    }
}
