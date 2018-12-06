package com.gosuncn.shop.controller;

/**
 * @author: chenxihua
 * @Date: 2018-11-21:8:46
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 这个controoler是直接做页面跳转的，其它的Controller就做成 @Restcontroller
 */
@Slf4j
@Controller
@RequestMapping("/common")
public class CommonController {


    @RequestMapping(value = "/register")
    public String register(){
        return "user/register";
    }

    @RequestMapping(value = "/applySchool")
    public String upSchool(){
        return "user/schools";
    }


    @RequestMapping("/loginIndex")
    public String loginSuccess(){
        log.info("----> : can go this...");
        return "pages/userLogin";
    }
}
