package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:35
 */
public interface CommentsDao extends JpaRepository<Comments, Integer>, JpaSpecificationExecutor<Comments>, Serializable {
}
