package com.bigfong.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 切面连接点，通过它可以获得代理的业务方法的所有信息
 */
public interface JoinPoint {
    Method getMethod();//业务方法本身
    Object[] getArguments();//连接的实参列表
    Object getThis();//该方法所属的实例对象

    //在JoinPoint中添加自定义属性
    void setUserAttributre(String key,Object value);
    //从一个已知的自定义属性中获取一个属性值
    Object getUserAttribute(String key);
}
