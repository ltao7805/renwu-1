package com.ltao.pmai.mapper;


import com.ltao.pmai.model.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {

    User Login(@Param("user") User user);
}