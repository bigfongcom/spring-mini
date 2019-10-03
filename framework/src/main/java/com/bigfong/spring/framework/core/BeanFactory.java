package com.bigfong.spring.framework.core;

/**
 * 单例工厂的顶层设计
 * @author bigfong
 * @since 2019/10/2
 */
public interface BeanFactory {

    /**
     * 根据beanName从IOC容器中获得一个实例Bean
     * @param beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;

    public Object getBean(Class<?> beanClass) throws Exception;
}
