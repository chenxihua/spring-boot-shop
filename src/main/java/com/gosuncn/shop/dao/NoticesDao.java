package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Notices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @Author: chenxihua
 * @Date: 2019/2/22:9:14
 * @Version 1.0
 **/
public interface NoticesDao extends JpaRepository<Notices, Integer>, JpaSpecificationExecutor<Notices> {
}
