package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @Author: chenxihua
 * @Date: 2019/2/11:15:01
 * @Version 1.0
 **/
public interface UserRolesDao extends JpaRepository<UserRoles, Integer>, JpaSpecificationExecutor<UserRoles> {
}
