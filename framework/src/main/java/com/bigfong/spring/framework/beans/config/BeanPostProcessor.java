package com.bigfong.spring.framework.beans.config;

/**
 * 类似原生spring中的BeanPostProcessor为对象实始化事件设置的一种回调机制
 * @author bigfong
 * @since 2019/10/3
 */
public class BeanPostProcessor {

    /**
     * 为在Bean的初始化之前提供回调入口
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object postProcessBeforeInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }

    /**
     * 为在Bean的初始化之后提供回调入口
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object postProcessAfterInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }
}
