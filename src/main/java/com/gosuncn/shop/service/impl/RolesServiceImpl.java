package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.RolesDao;
import com.gosuncn.shop.entities.Roles;
import com.gosuncn.shop.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:16:07
 * @Version 1.0
 **/
@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    RolesDao rolesDao;

    /**
     * 根据rolesid，找到具体的roles
     * @param rolesId
     * @return
     */
    @Override
    public Roles getRolesById(Integer rolesId) {
        Roles roles = new Roles();
        roles.setId(rolesId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Roles> example = Example.of(roles, matcher);
        Optional<Roles> one = rolesDao.findOne(example);
        return one.orElse(null);
    }

    /**
     * 这里用于超级管理员，显示所有角色
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<Roles> getAllRoles(Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Roles> rolesPage = rolesDao.findAll(pageable);
        return rolesPage;
    }

    @Override
    public List<Roles> getAllRolesList() {
        List<Roles> rolesList = rolesDao.findAll();
        return rolesList;
    }

    /**
     * 新增一个角色
     * @param roles
     * @return
     */
    @Override
    public boolean addRoles(Roles roles) {
        Roles save = rolesDao.save(roles);
        if (save!=null){
            return true;
        }else{
            return false;
        }
    }
}
