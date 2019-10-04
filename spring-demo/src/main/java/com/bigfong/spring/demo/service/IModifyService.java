package com.bigfong.spring.demo.service;

/**
 * 增册改查业务接口
 *
 * @author bigfong
 * @since 2019/10/3
 */
public interface IModifyService {

    public String add(String name, String addr) throws Exception;

    public String edit(Integer id, String name);

    public String remove(Integer id);
}
