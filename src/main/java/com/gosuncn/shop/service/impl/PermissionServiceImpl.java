package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.PermissionDao;
import com.gosuncn.shop.entities.Permission;
import com.gosuncn.shop.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:16:19
 * @Version 1.0
 **/
@Service
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    PermissionDao permissionDao;

    /**
     * 根据具体的permission 的id，找到某一个permission
     * @param permissionId
     * @return
     */
    @Override
    public Permission getPermissionById(Integer permissionId) {
        Permission permission = new Permission();
        permission.setId(permissionId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Permission> example = Example.of(permission, matcher);
        Optional<Permission> one = permissionDao.findOne(example);
        return one.orElse(null);
    }

    /**
     * 这里是给超级管理员使用，查询所有权限
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<Permission> getPermissions(Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Permission> permissions = permissionDao.findAll(pageable);
        return permissions;
    }
}
