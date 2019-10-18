package com.bigfong.spring.framework.mybatis.utils;

import com.bigfong.spring.framework.mybatis.io.Resources;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSession;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSessionFactory;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @Description TODO
 * @Author Bigfong
 * @Date 2019/10/17
 **/
public class SqlSessionUtil {
    private static SqlSession sqlSession;
    private SqlSessionUtil() {
    }

    /**
         InputStream in = Resources.getResourceAsStream("mybatis.xml");
         SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
         SqlSessionFactory factory = builder.build(in);
         SqlSession session = factory.openSession();
         UserMapper userDao = session.getMapper(UserMapper.class);
        List<User> users = userDao.findAll();
            for (User user:users){
            System.out.println(user);
        }
        session.close();
        in.close();
     * @return
     */
    public static SqlSession getSqlSessionInstance(){
        synchronized (SqlSessionUtil.class){
            if (sqlSession==null){
                InputStream in = Resources.getResourceAsStream("mybatis.xml");
                SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
                SqlSessionFactory factory = builder.build(in);
                sqlSession = factory.openSession();
            }
        }
        return sqlSession;
    }
}
