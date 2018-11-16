package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:42
 */
public interface PermissionDao extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission>, Serializable {
}
