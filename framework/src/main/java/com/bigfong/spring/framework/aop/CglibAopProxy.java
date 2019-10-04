package com.bigfong.spring.framework.aop;

import com.bigfong.spring.framework.aop.support.AdvisedSupport;

/**使用CGlib Api生成代理类
 * @author bigfong
 * @since 2019/10/4
 */
public class CglibAopProxy implements AopProxy{
    private AdvisedSupport config;

    public CglibAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
