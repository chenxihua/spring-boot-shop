package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Essays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:38
 */
public interface EssaysDao extends JpaRepository<Essays, Integer>, JpaSpecificationExecutor<Essays>, Serializable {
}
