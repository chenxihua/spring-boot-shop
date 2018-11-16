package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:33
 */
public interface CollectionDap extends JpaRepository<Collection, Integer>, JpaSpecificationExecutor<Collection>, Serializable {
}
