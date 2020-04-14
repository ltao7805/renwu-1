package com.ltao.pmai.controller;

import com.ltao.pmai.core.Result;
import com.ltao.pmai.core.ResultGenerator;
import com.ltao.pmai.model.User;
import com.ltao.pmai.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pmai")
public class ControllerTest {

    @Autowired
    private Services services;

    @GetMapping
    public Result hello(){
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("login/{name}/{pwd}")
    public Result UserLogin(@PathVariable String name, @PathVariable String pwd){
        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        return services.Login(user);
    }

}
