package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:24
 */
public interface AdministratorDao extends JpaRepository<Administrator, Integer>, JpaSpecificationExecutor<Administrator>, Serializable {
}
