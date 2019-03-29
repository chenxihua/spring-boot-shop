package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.RolesPermission;

import java.util.List;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:16:15
 * @Version 1.0
 **/
public interface RolesPermissionService {


    /**
     * 根据角色id，找到这个角色的所有权限
     * @param rolesId
     * @return
     */
    public List<RolesPermission> getRolesPermissions(Integer rolesId);

}
