package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.Permission;
import org.springframework.data.domain.Page;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:16:15
 * @Version 1.0
 **/
public interface PermissionService {


    /**
     * 根据id，找到某一个具体的权限
     * @param permissionId
     * @return
     */
    public Permission getPermissionById(Integer permissionId);


    /**
     * 这里是给超级管理员使用，查询所有权限
     * @param page
     * @param limit
     * @return
     */
    public Page<Permission> getPermissions(Integer page, Integer limit);
}
