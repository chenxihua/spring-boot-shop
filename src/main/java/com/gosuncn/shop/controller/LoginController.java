package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.ProvinceDao;
import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:10:24
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    SchoolService schoolService;
    @Autowired
    ProvinceDao provinceDao;




    @RequestMapping("/register")
    public String register(){
        return "user/register";
    }

    @RequestMapping("/submitSchool")
    public String upSchool(){
        return "user/schools";
    }

    @ResponseBody
    @RequestMapping(value = "/loadSchools", method = RequestMethod.POST)
    public Map<String, Object> getAllSchools(){
        Map<String, Object> map = new HashMap<>();
        List<School> schools = schoolService.findAllSchoolInfos();
        map.put("code", 0);
        map.put("data", schools);
        map.put("success", true);
        map.put("msg", "查询所有学校");
        return map;
    }


    @ResponseBody
    @GetMapping(value = "/loadPartion")
    public Map<String, Object> getAllPartions(){
        Map<String, Object> map = new HashMap<>();
        List<Province> provinces = provinceDao.findAll();
        map.put("code", 0);
        map.put("data", provinces);
        map.put("success", true);
        map.put("msg", "查询所有省份");
        return map;
    }

    @PostMapping(name = "/savaSchool")
    public Map<String, Object> saveApplySchool(School school){
        Map<String, Object> map = new HashMap<>();
        boolean info = schoolService.saveSchoolInfo(school);
        if(info){
            map.put("code", 0);
            map.put("success", true);
            map.put("msg", "保存成功");
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "保存失败");
        }
        return map;
    }



}





