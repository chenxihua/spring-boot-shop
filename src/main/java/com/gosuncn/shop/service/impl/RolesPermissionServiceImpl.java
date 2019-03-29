package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.RolesPermissionDao;
import com.gosuncn.shop.entities.RolesPermission;
import com.gosuncn.shop.service.RolesPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:16:31
 * @Version 1.0
 **/
@Service
public class RolesPermissionServiceImpl implements RolesPermissionService {

    @Autowired
    RolesPermissionDao rolesPermissionDao;

    /**
     * 根据角色id，找到这个角色所有的权限id，
     * @param rolesId
     * @return
     */
    @Override
    public List<RolesPermission> getRolesPermissions(Integer rolesId) {
        RolesPermission rolesPermission = new RolesPermission();
        rolesPermission.setRolesId(rolesId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<RolesPermission> example = Example.of(rolesPermission, matcher);
        List<RolesPermission> all = rolesPermissionDao.findAll(example);
        return all;
    }
}
