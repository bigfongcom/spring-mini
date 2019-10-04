package com.bigfong.spring.framework.aop;

/**
 * 代理工厂顶层,提供获取代理对像的顶层入口
 * 子类:
 * CglibAopProxy:实现CGlib
 * JdkDynamicAopProxy:实现Jdk动态理
 *
 * 默认使用JDK动态代理
 * @author bigfong
 * @since 2019/10/4
 */
public interface AopProxy {
    //获得一个代理对象
    Object getProxy();
    //通过自定义加载器获得一个代理对象
    Object getProxy(ClassLoader classLoader);
}
