package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:44
 */
public interface RolePermissionDao extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission>, Serializable {
}
