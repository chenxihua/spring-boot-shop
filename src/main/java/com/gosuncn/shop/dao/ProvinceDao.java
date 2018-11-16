package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:43
 */
public interface ProvinceDao extends JpaRepository<Province, Integer>, JpaSpecificationExecutor<Province>, Serializable {
}
