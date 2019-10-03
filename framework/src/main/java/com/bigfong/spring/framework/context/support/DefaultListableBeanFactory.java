package com.bigfong.spring.framework.context.support;

import com.bigfong.spring.framework.beans.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ioc容器子类的典型代表
 * @author bigfong
 * @since 2019/10/2
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {
    //存储注册信息的BeanDefinition
    protected final Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
}
