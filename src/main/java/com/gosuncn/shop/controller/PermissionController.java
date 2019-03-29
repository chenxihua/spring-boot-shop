package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.PermissionDao;
import com.gosuncn.shop.entities.Permission;
import com.gosuncn.shop.entities.Roles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: chenxihua
 * @Date: 2019/2/12:20:08
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionDao permissionDao;


    /**
     * 分页查询所有权限
     * @param queryParams
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/permission_list")
    public Map<String, Object> permissionList(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("perName");

            // 创建查询规格对象
            Specification<Permission> specification = (Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;
                if (keyword != null && !"".equals(keyword)) {
                    Path path = root.get("name");
                    predicate = cb.like(path, "%" + keyword + "%");
                }
                return predicate;
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<Permission> permissions = permissionDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", permissions.getTotalElements());
            result.put("data", permissions.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 删除某一个权限
     * @param perId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/permission_delete/{perId}")
    public Integer permissionDelete(@PathVariable Integer perId){
        try{
            permissionDao.deleteById(perId);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}
