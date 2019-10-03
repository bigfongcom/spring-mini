package com.bigfong.spring.framework.context.support;

/**
 * Ioc容器实现类的顶层抽像类
 * 实现Ioc容器相关的公共逻辑
 * @author bigfong
 * @since 2019/10/2
 */
public abstract class AbstractApplicationContext {
    /**
     * 受保护的方法，只提供给子类重写
     * @throws Exception
     */
    public void refresh() throws Exception{};
}
