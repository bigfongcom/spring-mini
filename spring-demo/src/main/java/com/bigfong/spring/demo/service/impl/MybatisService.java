package com.bigfong.spring.demo.service.impl;

import com.bigfong.spring.demo.entity.User;
import com.bigfong.spring.demo.mapper.UserMapper;
import com.bigfong.spring.demo.service.IModifyService;
import com.bigfong.spring.demo.service.IMybatisService;
import com.bigfong.spring.framework.annotation.Autowired;
import com.bigfong.spring.framework.annotation.Service;
import com.bigfong.spring.framework.mybatis.io.Resources;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSession;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSessionFactory;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @author bigfong
 * @since 2019/10/16
 */
@Service
public class MybatisService implements IMybatisService {

    @Autowired
    UserMapper userMapper;

    @Override
    public String test() throws Exception {
        /*InputStream in = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        SqlSession session = factory.openSession();
        UserMapper userDao = session.getMapper(UserMapper.class);*/
        List<User> users = userMapper.findAll();
        for (User user:users){
            System.out.println(user);
        }
        /*session.close();
        in.close();*/

        return null;
    }
}
