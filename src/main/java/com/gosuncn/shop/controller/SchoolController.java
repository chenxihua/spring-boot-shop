package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.ProvinceService;
import com.gosuncn.shop.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


/**
 * @author: chenxihua
 * @Date: 2018-11-20:16:09
 */

@Slf4j
@Controller
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    SchoolService schoolService;
    @Autowired
    ProvinceService provinceService;

    @Autowired
    SchoolDao schoolDao;


    /**
     * 获取所有学校，注册时，让学生挑选自己所在的学校
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getSchools")
    public Map<String, Object> getAllSchools(){
        Map<String, Object> map = new HashMap<>();
        try {
            List<School> schools = schoolService.findAllSchoolInfos();
            map.put("code", 0);
            map.put("data", schools);
            map.put("msg", "查询所有学校");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "查询失败");
        }
        return map;
    }

    /**
     * 申请新学校，加载所有省份。
     * @return
     */
    @ResponseBody
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
    @ResponseBody
    @PostMapping(value = "/saveSchool")
    public Integer saveApplySchool(School school){
        Map<String, Object> map = new HashMap<>();
        try {
            school.setStatus(1);
            school.setApplyTime(new Date());
            schoolService.saveSchoolInfo(school);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;

    }


    /**
     * 根据学校id，找到具体id的学校的所有信息。
     * @param schId
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getSchById/{schId}")
    public Map<String, Object> getSchoolById(@PathVariable("schId")Integer schId){
        Map<String, Object> map = new HashMap<>();
        try{
            School school = schoolService.getSchoolById(schId);
            map.put("code", 0);
            map.put("success", true);
            map.put("data", school);
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", 500);
            map.put("success", false);
        }
        return map;
    }


    //   ----------------       分割线      ----------------------------

    /**
     * 查询所有学生提交的学校审核
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/school_list")
    public Map<String, Object> schoolList(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("schoolName");

            // 创建查询规格对象
            Specification<School> specification = (Root<School> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("fullName").as(String.class), "%"+keyword+"%"));
                }
                // predicates.add(cb.equal(root.get("status").as(Integer.class), 1));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "applyTime");
            Page<School> schools = schoolDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", schools.getTotalElements());
            result.put("data", schools.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，查询待审核学校错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 移除学校
     * @param schId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete_school/{schId}")
    public Integer deleteSchool(@PathVariable Integer schId){
        try {
            schoolDao.deleteById(schId);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 重新审核学校
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/review_school/{schId}")
    public Integer reviewSchool(@PathVariable Integer schId){
        try {
            School school = schoolDao.getOne(schId);
            school.setStatus(1);
            school.setApplyTime(new Date());
            schoolDao.saveAndFlush(school);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 页面跳转， 用于审核学校，
     * @param schId
     * @param model
     * @return
     */
    @RequestMapping(value = "/toCheckSchool/{schId}")
    public String toCheckSchool(@PathVariable Integer schId, Model model){
        School school = schoolDao.getOne(schId);
        model.addAttribute("schoolId", school.getId());
        return "manager/addCheckResult";
    }


    /**
     * 用于管理员，更新学校的申请状态
     * @param yesBtn
     * @param school
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkResults/{yesBtn}")
    public Integer checkResults(@PathVariable Integer yesBtn, School school){
        try {
            School one = schoolDao.getOne(school.getId());
            one.setDescription(school.getDescription());
            one.setStatus(yesBtn);
            schoolDao.saveAndFlush(one);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 跳转到新增学校信息的页面
     * @return
     */
    @RequestMapping(value = "/to_add_school")
    public String toAddSchool(){
        return "user/schools";
    }





}
