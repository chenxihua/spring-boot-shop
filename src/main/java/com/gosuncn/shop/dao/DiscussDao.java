package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Discuss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:37
 */
public interface DiscussDao extends JpaRepository<Discuss, Integer>, JpaSpecificationExecutor<Discuss> {
}
