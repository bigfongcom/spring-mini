package com.bigfong.spring.demo.mapper;

import com.bigfong.spring.demo.entity.User;

import java.util.List;

public interface UserMapper {
    //@Select("select * from user")
    List<User> findAll();
}