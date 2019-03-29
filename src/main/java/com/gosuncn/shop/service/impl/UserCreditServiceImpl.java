package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.UserCreditDao;
import com.gosuncn.shop.entities.UserCredit;
import com.gosuncn.shop.service.UserCreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: chenxihua
 * @Date: 2019/3/11:16:07
 * @Version 1.0
 **/

@Slf4j
@Service
public class UserCreditServiceImpl implements UserCreditService {

    @Autowired
    UserCreditDao userCreditDao;

    /**
     * 查询统计迟到次数
     *
     * @param userId
     * @param status
     * @return
     */
    @Override
    public long countLates(Integer userId, Integer status) {
        UserCredit userCredit = new UserCredit();
        userCredit.setPublishUser(userId);
        userCredit.setIsLate(status);
        userCredit.setStatus(1);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, exampleMatcher);
        try {
            long count = userCreditDao.count(example);
            return count;
        }catch (Exception e){
            e.printStackTrace();
            log.error("出现异常了，查什么查");
        }
        return -1;
    }

    /**
     * 查询统计商品质量好坏的次数
     *
     * @param userId
     * @param status
     * @return
     */
    @Override
    public long countQualitys(Integer userId, Integer status) {
        UserCredit userCredit = new UserCredit();
        userCredit.setPublishUser(userId);
        userCredit.setIsQuality(status);
        userCredit.setStatus(1);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, exampleMatcher);
        try {
            long count = userCreditDao.count(example);
            return count;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 查询统计卖家服务态度好坏的次数
     *
     * @param userId
     * @param status
     * @return
     */
    @Override
    public long countServices(Integer userId, Integer status) {
        UserCredit userCredit = new UserCredit();
        userCredit.setPublishUser(userId);
        userCredit.setIsService(status);
        userCredit.setStatus(1);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, exampleMatcher);
        try {
            long count = userCreditDao.count(example);
            return count;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 查询统计卖家发布的商品是否符合描述
     *
     * @param userId
     * @param status
     * @return
     */
    @Override
    public long countDescriptions(Integer userId, Integer status) {
        UserCredit userCredit = new UserCredit();
        userCredit.setPublishUser(userId);
        userCredit.setIsDescription(status);
        userCredit.setStatus(1);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, exampleMatcher);
        try {
            long count = userCreditDao.count(example);
            return count;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 查看自己这次买卖中，买家给的信用积分
     *
     * @param publishMan
     * @param orderId
     * @return
     */
    @Override
    public UserCredit searchOne(Integer publishMan, Integer orderId) {
        UserCredit userCredit = new UserCredit();
        userCredit.setPublishUser(publishMan);
        userCredit.setOrderId(orderId);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, exampleMatcher);
        try {
            Optional<UserCredit> one = userCreditDao.findOne(example);
            return one.orElse(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
