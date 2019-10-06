package com.bigfong.spring.framework.orm.framework;

/**
 * sql排序组件
 *
 * @author bigfong
 * @since 2019/10/6
 */
public class Order {
    private boolean ascending;//升序or降序
    private String propertyName;//哪个字段升序/降序

    @Override
    public String toString() {
        return propertyName + ' ' + (ascending ? "asc" : "desc");
    }

    protected Order(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    public static Order asc(String propertyName){
        return new Order(propertyName,true);
    }

    public static Order desc(String propertyName){
        return new Order(propertyName,false);
    }
}
