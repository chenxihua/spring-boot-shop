package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:39
 */
public interface GoodsDao extends JpaRepository<Goods, Integer>, JpaSpecificationExecutor<Goods>, Serializable {
}
