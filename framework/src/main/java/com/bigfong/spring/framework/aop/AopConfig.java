package com.bigfong.spring.framework.aop;

import lombok.Data;

/**
 * Aop配置封装
 */
@Data
public class AopConfig {
    //以下配置与properties文件中的属性一一对应
    private String pointCut;//切面表达式
    private String aspectClass;//切面类
    private String aspectBefore;//切面前置通知名
    private String aspectAfter;//切面后置通知名
    private String aspectAfterThrow;//切面异常通知名
    private String aspectAfterThrowingName;//需要通知的异常类型
}
