package com.ltao.pmai;

import com.alibaba.fastjson.JSON;
import com.ltao.pmai.core.Result;
import com.ltao.pmai.core.ResultCode;
import com.ltao.pmai.core.ResultGenerator;
import com.ltao.pmai.mapper.UserMapper;
import com.ltao.pmai.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PmaiApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        User user = new User();
        user.setName("ltao");
        user.setPwd("7805");
        User login = userMapper.Login(user);

        Result<User> userResult = ResultGenerator.genSuccessResult("登录成功",login);
        System.out.println(JSON.toJSONString(userResult));
    }

}
