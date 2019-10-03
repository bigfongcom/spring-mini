package com.bigfong.spring.framework.beans;

/**
 * 封装创建后的对象实例
 * 代理对像(Proxy Object)或原生对象(Original Object)
 * @author bigfong
 * @since 2019/10/2
 */
public class BeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * 返回代理以后的Class
     * 可能会是这个 $Proxy0
     * @return
     */
    public Class<?> getWreappedClass() {
        return this.wrappedInstance.getClass();
    }


}
