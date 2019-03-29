package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:39
 */
public interface ItemsDao extends JpaRepository<Items, Integer>, JpaSpecificationExecutor<Items> {
}
