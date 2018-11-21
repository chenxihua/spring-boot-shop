package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.ProvinceDao;
import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.SchoolService;
import com.gosuncn.shop.service.UserService;
import com.gosuncn.shop.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:10:24
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    UserService userService;

    PasswordHelper passwordHelper;


    @RequestMapping("/register")
    public String register(){
        return "user/register";
    }

    @RequestMapping("/applySchool")
    public String upSchool(){
        return "user/schools";
    }


    /**
     * 注册系统新用户（学生注册）
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveByRegister")
    public Map<String, Object> saveUser(User user){
        Map<String, Object> map =  new HashMap<>();
        String oldPassword = user.getPassword();

        Map<String, Object> passwordMap = PasswordHelper.encryptPassword(oldPassword);
        boolean canable = (boolean) passwordMap.get("canable");
        if(canable){
            user.setPassword((String) passwordMap.get("newPassword"));
            user.setSalt((String) passwordMap.get("salt"));
            user.setStatus(1);
            user.setSignTime(new Date());
            boolean userFlag = userService.saveOneUser(user);
            if (userFlag){
                map.put("code", 0);
                map.put("success", true);
                map.put("msg", "你注册用户已成功");
            }else {
                map.put("code", 0);
                map.put("success", false);
                map.put("msg", "你注册用户已失败");
            }
        }else{
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", (String) passwordMap.get("message"));
        }
        return map;
    }







}





