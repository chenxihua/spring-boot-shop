package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Partion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:41
 */
public interface PartionDao extends JpaRepository<Partion, Integer>, JpaSpecificationExecutor<Partion> {
}
