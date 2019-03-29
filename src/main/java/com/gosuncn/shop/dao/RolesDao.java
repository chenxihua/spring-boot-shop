package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @Author: chenxihua
 * @Date: 2019/1/25:15:00
 * @Version 1.0
 **/
public interface RolesDao extends JpaRepository<Roles, Integer>, JpaSpecificationExecutor<Roles> {
}
