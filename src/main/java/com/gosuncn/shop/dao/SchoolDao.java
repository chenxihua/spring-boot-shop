package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:47
 */
public interface SchoolDao extends JpaRepository<School, Integer>, JpaSpecificationExecutor<School> {
}
