package com.bigfong.spring.demo.mapper;

import com.bigfong.spring.demo.entity.User;
import com.bigfong.spring.framework.annotation.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //@Select("select * from user")
    List<User> findAll();
}