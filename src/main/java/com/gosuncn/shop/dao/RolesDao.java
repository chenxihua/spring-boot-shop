package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:45
 */
public interface RolesDao extends JpaRepository<Roles, Integer>, JpaSpecificationExecutor<Roles>, Serializable {
}
