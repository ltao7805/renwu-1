package com.ltao.pmai.service;

import com.ltao.pmai.core.Result;
import com.ltao.pmai.core.ResultGenerator;
import com.ltao.pmai.mapper.UserMapper;
import com.ltao.pmai.model.User;
import com.ltao.pmai.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Services {
    @Autowired
    private UserMapper userMapper;

    public Result Login(User user){
        User login = userMapper.Login(user);
        if(login!=null){
            //用户信息保存到token
            String token = JwtUtils.getJwtToken(user);
            return  ResultGenerator.genSuccessResult("登录成功",token);
        }else{
            return  ResultGenerator.genFailResult("账号或密码错误!");
        }
    }

}
