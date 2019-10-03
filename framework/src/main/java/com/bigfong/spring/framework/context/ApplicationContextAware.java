package com.bigfong.spring.framework.context;

/**
 * 通过解耦方式获得IOC容器的顶层设计
 * 后面将通过一个监听器去扫描所有的类，只要实现了此接口，
 * 将自动调用setApplicationContext()方法，从而将IOC容器注入目标类中
 * @author bigfong
 * @since 2019/10/3
 */
public interface ApplicationContextAware {
    void setApplicationContext(ApplicationContext applicationContext);
}
