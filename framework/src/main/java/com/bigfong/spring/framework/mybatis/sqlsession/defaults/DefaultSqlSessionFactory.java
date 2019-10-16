package com.bigfong.spring.framework.mybatis.sqlsession.defaults;


import com.bigfong.spring.framework.mybatis.cfg.Configuration;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSession;
import com.bigfong.spring.framework.mybatis.sqlsession.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration cfg;

    public DefaultSqlSessionFactory(Configuration cfg){
        this.cfg = cfg;
    }
    /**
     * 用于创建一个新的操作数据库对象
     * @return
     */
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(cfg);
    }
}