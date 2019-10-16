package com.bigfong.spring.framework.mybatis.io;

import java.io.InputStream;

/**
 * mybatis-资源类封装
 * @author bigfong
 * @since 2019/10/16
 */
public class Resources {
    /**
     * 根据传入的参数，获取一个字节输入流
     * @param filePath
     * @return
     */
    public static InputStream getResourceAsStream(String filePath){
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }
}