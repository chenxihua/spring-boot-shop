package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: chenxihua
 * @Date: 2019/3/10:16:35
 * @Version 1.0
 **/
public interface UserCreditDao extends JpaRepository<UserCredit, Integer>, JpaSpecificationExecutor<UserCredit> {
}
