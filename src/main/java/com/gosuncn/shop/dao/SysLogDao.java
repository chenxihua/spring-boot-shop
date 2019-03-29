package com.gosuncn.shop.dao;

import com.gosuncn.shop.entities.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: chenxihua
 * @Date: 2019/2/25:16:46
 * @Version 1.0
 **/
public interface SysLogDao extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {
}
