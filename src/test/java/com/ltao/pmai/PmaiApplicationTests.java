package com.ltao.pmai;


import com.ltao.pmai.mapper.UserMapper;
import com.ltao.pmai.model.User;
import com.ltao.pmai.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class PmaiApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        Date date = new Date(System.currentTimeMillis()+2000*60);
        System.out.println(date);
    }

}
