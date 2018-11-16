package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:32
 */
public interface CartDao extends JpaRepository<Cart, Integer>, JpaSpecificationExecutor<Cart>, Serializable {
}
