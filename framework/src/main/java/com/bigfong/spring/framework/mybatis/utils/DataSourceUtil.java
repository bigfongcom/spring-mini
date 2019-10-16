package com.bigfong.spring.framework.mybatis.utils;

import com.bigfong.spring.framework.mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataSourceUtil {

    /**
     * 用于获取一个连接
     * @param cfg
     * @return
     */
    public static Connection getConnection(Configuration cfg){
        try {
            Class.forName(cfg.getDriver());
            return DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}