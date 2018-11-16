package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:40
 */
public interface OrdersDao extends JpaRepository<Orders, Integer>, JpaSpecificationExecutor<Orders>, Serializable {
}
