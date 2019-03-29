package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.RolesPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @Author: chenxihua
 * @Date: 2019/1/25:15:02
 * @Version 1.0
 **/
public interface RolesPermissionDao extends JpaRepository<RolesPermission, Integer>, JpaSpecificationExecutor<RolesPermission> {
}
