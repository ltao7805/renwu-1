package com.ltao.pmai.model;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

@Component
public class User {
    private Integer id;

    private String name;

    private String pwd;

    private Integer rid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}