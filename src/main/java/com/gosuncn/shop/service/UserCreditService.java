package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.UserCredit;

/**
 * @Author: chenxihua
 * @Date: 2019/3/11:16:01
 * @Version 1.0
 **/
public interface UserCreditService {


    /**
     * 查询统计迟到次数
     * @param userId
     * @param status
     * @return
     */
    public long countLates(Integer userId, Integer status);

    /**
     * 查询统计商品质量好坏的次数
     * @param userId
     * @param status
     * @return
     */
    public long countQualitys(Integer userId, Integer status);

    /**
     * 查询统计卖家服务态度好坏的次数
     * @param userId
     * @param status
     * @return
     */
    public long countServices(Integer userId, Integer status);

    /**
     * 查询统计卖家发布的商品是否符合描述
     * @param userId
     * @param status
     * @return
     */
    public long countDescriptions(Integer userId, Integer status);


    /**
     * 查看自己这次买卖中，买家给的信用积分
     * @param publishMan
     * @param orderId
     * @return
     */
    public UserCredit searchOne(Integer publishMan, Integer orderId);



}
