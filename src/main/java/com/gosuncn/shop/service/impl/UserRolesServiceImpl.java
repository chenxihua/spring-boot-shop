package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.UserRolesDao;
import com.gosuncn.shop.entities.UserRoles;
import com.gosuncn.shop.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:15:53
 * @Version 1.0
 **/
@Service
public class UserRolesServiceImpl implements UserRolesService {

    @Autowired
    UserRolesDao userRolesDao;

    /**
     * 根据用户id，找到这个用户的所有角色
     * @param userId
     * @return
     */
    @Override
    public List<UserRoles> getUserRoles(Integer userId) {
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(userId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserRoles> example = Example.of(userRoles, matcher);
        List<UserRoles> all = userRolesDao.findAll(example);
        return all;
    }

}
