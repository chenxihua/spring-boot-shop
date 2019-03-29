package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.Roles;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:16:05
 * @Version 1.0
 **/
public interface RolesService {


    /**
     * 根据rolesid， 查找到某一个roles
     * @param rolesId
     * @return
     */
    public Roles getRolesById(Integer rolesId);

    /**
     * 这里用于超级管理员，显示所有角色
     * @param page
     * @param limit
     * @return
     */
    public Page<Roles> getAllRoles(Integer page, Integer limit);

    public List<Roles> getAllRolesList();


    /**
     * 新增一个角色
     * @param roles
     * @return
     */
    public boolean addRoles(Roles roles);
}
