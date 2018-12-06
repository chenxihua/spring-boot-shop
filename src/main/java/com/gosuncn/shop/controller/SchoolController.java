package com.gosuncn.shop.controller;

import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.service.ProvinceService;
import com.gosuncn.shop.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: chenxihua
 * @Date: 2018-11-20:16:09
 */

@Slf4j
@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    SchoolService schoolService;
    @Autowired
    ProvinceService provinceService;


    /**
     * 获取所有学校，注册时，让学生挑选自己所在的学校
     * @return
     */
    @GetMapping(value = "/getSchools")
    public Map<String, Object> getAllSchools(){
        Map<String, Object> map = new HashMap<>();
        List<School> schools = schoolService.findAllSchoolInfos();
        for (School school : schools) {
            log.info("---> " +school.getFullName());
        }
        map.put("code", 0);
        map.put("data", schools);
        map.put("success", true);
        map.put("msg", "查询所有学校");
        return map;
    }

    /**
     * 申请新学校，加载所有省份。
     * @return
     */
    @GetMapping(value = "/getPartions")
    public Map<String, Object> getAllPartions(){
        Map<String, Object> map = new HashMap<>();
        List<Province> provinces = provinceService.getAllProvince();
        map.put("code", 0);
        map.put("data", provinces);
        map.put("success", true);
        map.put("msg", "查询所有省份");
        return map;
    }

    /**
     * 保存申请新学校，这步需要管理员审核，将新申请学校的status更改成1
     * @param school
     * @return
     */
    @PostMapping(value = "/saveSchool")
    public Map<String, Object> saveApplySchool(School school){
        Map<String, Object> map = new HashMap<>();
        school.setStatus(1);
        school.setApplyTime(new Date());
        boolean saveSchoolInfo = schoolService.saveSchoolInfo(school);
        if(saveSchoolInfo){
            map.put("code", 0);
            map.put("success", true);
            map.put("msg", "申请学校成功");
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "申请学校失败");
        }
        return map;
    }

}
