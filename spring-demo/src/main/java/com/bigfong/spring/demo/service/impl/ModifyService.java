package com.bigfong.spring.demo.service.impl;

import com.bigfong.spring.demo.service.IModifyService;
import com.bigfong.spring.framework.annotation.Service;

/**
 * @author bigfong
 * @since 2019/10/3
 */
@Service
public class ModifyService implements IModifyService {
    @Override
    public String add(String name, String addr) throws Exception{
        throw new Exception("抛出异常，测试切面通知是否生效");
        //return "ModifyService add:name=" + name + ",addr=" + addr;
    }

    @Override
    public String edit(Integer id, String name) {
        return "ModifyService edit:name=" + name + ",id=" + id;
    }

    @Override
    public String remove(Integer id) {
        return "ModifyService remove: id=" + id;
    }
}
