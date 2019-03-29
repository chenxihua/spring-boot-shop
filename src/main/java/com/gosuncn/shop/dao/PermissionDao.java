package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @Author: chenxihua
 * @Date: 2019/1/25:15:01
 * @Version 1.0
 **/
public interface PermissionDao extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {
}
