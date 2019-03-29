package com.gosuncn.shop.controller;

import com.gosuncn.shop.annonation.MyLog;
import com.gosuncn.shop.dao.PermissionDao;
import com.gosuncn.shop.dao.RolesDao;
import com.gosuncn.shop.dao.RolesPermissionDao;
import com.gosuncn.shop.dao.UserRolesDao;
import com.gosuncn.shop.entities.Permission;
import com.gosuncn.shop.entities.Roles;
import com.gosuncn.shop.entities.RolesPermission;
import com.gosuncn.shop.entities.UserRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenxihua
 * @Date: 2019/2/12:19:36
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    RolesDao rolesDao;

    @Autowired
    PermissionDao permissionDao;
    @Autowired
    RolesPermissionDao rolesPermissionDao;
    @Autowired
    UserRolesDao userRolesDao;

    @MyLog(value = "查询操作list")
    @ResponseBody
    @RequestMapping(value = "/roles_list")
    public Map<String, Object> rolesList(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("rolesName");

            // 创建查询规格对象
            Specification<Roles> specification = (Root<Roles> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;
                if (keyword != null && !"".equals(keyword)) {
                    Path path = root.get("name");
                    predicate = cb.like(path, "%" + keyword + "%");
                }
                return predicate;
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<Roles> roles = rolesDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", roles.getTotalElements());
            result.put("data", roles.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }

    @MyLog(value = "删除操作roles")
    @ResponseBody
    @RequestMapping(value = "/roles_delete/{roleId}")
    public Integer deleteRoles(@PathVariable("roleId")Integer roleId){
        try{
            rolesDao.deleteById(roleId);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

//    @MyLog(value = "页面跳转")
    @RequestMapping(value = "/roles_view")
    public String getAddRoles(Integer rolesId, Model model){
        Roles roles = new Roles();
        if(rolesId != null){
            roles = rolesDao.getOne(rolesId);
        }
        model.addAttribute("roles", roles);
        return "admin/addRoles";
    }

    @MyLog(value = "roles更新")
    @ResponseBody
    @RequestMapping(value = "/roles_update")
    public Integer updateRoles(Roles roles){
        try {
            rolesDao.save(roles);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 角色分配权限跳转页面
     * @return
     */
//    @MyLog(value = "权限跳转页，")
    @RequestMapping(value = "/newRolesPer/{roleId}")
    public String rolesToPermission(Model model, @PathVariable Integer roleId){
        // 查询所有权限,用于给角色分配权限
        List<Permission> permissionList = permissionDao.findAll();
        model.addAttribute("permissions", permissionList);
        model.addAttribute("roleId", roleId);
        return "admin/toPermission";
    }

    /**
     * 用于显示这个角色所有的权限
     * @return
     */
    @MyLog(value = "显示所有permission")
    @ResponseBody
    @RequestMapping(value = "/showPermiss/{rolesVal}")
    public Map<String, Object> showPermiss(@RequestParam Map<String, Object> queryParams, @PathVariable Integer rolesVal){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            // 创建查询规格对象
            Specification<RolesPermission> specification = (Root<RolesPermission> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;
                predicate = cb.equal(root.get("rolesId").as(String.class), rolesVal);
                return predicate;
            };
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<RolesPermission> rolesPermissions = rolesPermissionDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", rolesPermissions.getTotalElements());
            result.put("data", rolesPermissions.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误,查询权限的权限失败");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 删除这个角色的某一个权限
     * @param id
     * @return
     */
    @MyLog(value = "删除某一个权限")
    @ResponseBody
    @RequestMapping(value = "/delete_roles_permiss/{id}")
    public Integer deleteRolesPermiss(@PathVariable Integer id){
        try{
            rolesPermissionDao.deleteById(id);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 根据权限id，显示这个权限的名称
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/permiss_name/{permissionId}")
    public Map<String, Object> permissName(@PathVariable Integer permissionId){
        Map<String, Object> result = new HashMap<>();
        try {
            Permission one = permissionDao.getOne(permissionId);
            result.put("code", 0);
            result.put("data", one);
            result.put("msg", "查询ok");
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询失败");
        }
        return result;
    }


    /**
     * 给一个角色，保存一个权限
     * @param perId
     * @param rolesId
     * @return
     */
    @MyLog(value = "保存一个权限")
    @ResponseBody
    @RequestMapping(value = "/addForPermiss")
    public Integer addForPermiss(@RequestParam Integer perId, @RequestParam Integer rolesId){

        RolesPermission rolesPermission = new RolesPermission();
        rolesPermission.setRolesId(rolesId);
        rolesPermission.setPermissionId(perId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<RolesPermission> example = Example.of(rolesPermission, matcher);
        try{
            boolean exists = rolesPermissionDao.exists(example);
            if (exists){
                return 1;
            }else{
                rolesPermission.setStatus(1);
                rolesPermissionDao.save(rolesPermission);
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 给用户配置角色，这里是跳转到配置角色页面
     * @return
     */
    @RequestMapping(value = "/userRoles/{userId}")
    public String UserRoles(@PathVariable Integer userId, Model model){
        List<Roles> roles = rolesDao.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("userId", userId);
        return "admin/toUserRoles";
    }


    /**
     * 显示一个用户的所有角色,用于管理员给后台管理员分配角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/showUserRoles/{userVal}")
    public Map<String, Object> showUserRoles(@RequestParam Map<String, Object> queryParams, @PathVariable Integer userVal){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            // 创建查询规格对象
            Specification<UserRoles> specification = (Root<UserRoles> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;
                predicate = cb.equal(root.get("userId").as(String.class), userVal);
                return predicate;
            };
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<UserRoles> userRoles = userRolesDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", userRoles.getTotalElements());
            result.put("data", userRoles.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误,查询角色的权限失败");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 用于显示这个角色的名称，这是根据rolesId，找到某一个roles，然后把它的名称显示出来
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/roles_name/{rolesId}")
    public Map<String, Object> rolesName(@PathVariable Integer rolesId){
        Map<String, Object> result = new HashMap<>();
        try {
            Roles one = rolesDao.getOne(rolesId);
            result.put("code", 0);
            result.put("data", one);
            result.put("msg", "查询ok");
        }catch (Exception e){
            result.put("code", 500);
            result.put("msg", "查询失败");
        }
        return result;
    }


    /**
     * 移除用户的某一角色
     * @return
     */
    @MyLog(value = "超级管理员移除用户某一个角色")
    @ResponseBody
    @RequestMapping(value = "/delete_user_roles/{id}")
    public Integer deleteUserRoles(@PathVariable Integer id){
        try {
            userRolesDao.deleteById(id);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 用户管理员分配用户角色，
     * @return
     */
    @MyLog(value = "超级管理员分配角色")
    @ResponseBody
    @RequestMapping(value = "/addForRoles")
    public Integer addForRoles(@RequestParam Integer rolesVal, @RequestParam Integer userVal){

        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(userVal);
        userRoles.setRolesId(rolesVal);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserRoles> example = Example.of(userRoles, matcher);
        try {
            boolean exists = userRolesDao.exists(example);
            if (exists){
                return 2;
            }else {
                userRoles.setStatus(1);
                userRolesDao.save(userRoles);
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }



}
