package com.bigfong.spring.framework.beans.config;

/**
 * 主要用于保存Bean相关的配置信息
 * （有来存储配置文件的信息，相当于保存在内存中的配置）
 * @author bigfong
 * @since 2019/10/2
 */
public class BeanDefinition {
    private String beanClassName;//原生Bean的全类名
    private boolean lazyinit = false;//标记是否延时加载
    private String factoryBeanName;//保存beanName,在Ioc容器中存储的key

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyinit() {
        return lazyinit;
    }

    public void setLazyinit(boolean lazyinit) {
        this.lazyinit = lazyinit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
