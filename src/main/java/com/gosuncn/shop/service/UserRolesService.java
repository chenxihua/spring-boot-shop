package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.UserRoles;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:15:51
 * @Version 1.0
 **/
public interface UserRolesService {

    /**
     * 根据用户id，找到这个用户的所有角色
     * @param userId
     * @return
     */
    public List<UserRoles> getUserRoles(Integer userId);


}
