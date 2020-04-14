package com.ltao.pmai.controller;

import com.ltao.pmai.core.Result;
import com.ltao.pmai.core.ResultGenerator;
import com.ltao.pmai.model.User;
import com.ltao.pmai.service.Services;
import com.ltao.pmai.util.JwtUtils;
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

    @PostMapping("login")
    public Result UserLogin(@RequestBody User user){
        return services.Login(user);
    }

    @GetMapping("getT/{token}")
    public Result<Object> getUser(@PathVariable String token){
        Object user = JwtUtils.getUserByJwtToken(token);
        return ResultGenerator.genSuccessResult("sucess", user);
    }


}
