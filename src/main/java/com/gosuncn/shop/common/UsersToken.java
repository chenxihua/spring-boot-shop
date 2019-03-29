package com.gosuncn.shop.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @Author: chenxihua
 * @Date: 2019/1/30:9:07
 * @Version 1.0
 * 新建org.apache.shiro.authc.UsernamePasswordToken的子类 UsersToken
 **/
//@Slf4j
//public class UsersToken extends UsernamePasswordToken {
//
//    //登录类型，判断是普通用户登录，还是管理员登录
//    private String loginType;
//
//    public UsersToken(final String username, final String password, String loginType){
//        super(username, password);
//        this.loginType = loginType;
//        log.info("--> usersToken, users");
//    }
//
//    public String getLoginType(){
//        return loginType;
//    }
//
//    public void setLoginType(String loginType){
//        this.loginType = loginType;
//    }
//}
