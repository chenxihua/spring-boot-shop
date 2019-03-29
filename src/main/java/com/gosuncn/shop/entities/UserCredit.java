package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: chenxihua
 * @Date: 2019/3/10:16:25
 * @Version 1.0
 *
 * 这个数据表，是给用户给商品发布者评信用积分。
 **/

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "user_credit")
@Entity
public class UserCredit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "publish_user")
    private Integer publishUser;
    @Column(name = "buy_user")
    private Integer buyUser;

    // 下面的orderId和上面的publishUser，buyUser用于唯一标识，信用只能评价一次
    @Column(name = "order_id")
    private Integer orderId;

    // 是否迟到
    @Column(name = "is_late")
    private Integer isLate;

    // 交易的商品质量是否好
    @Column(name = "is_quality")
    private Integer isQuality;

    // 是否符合商品发布的描述
    @Column(name = "is_description")
    private Integer isDescription;

    // 服务是否周到，或者是服务是否好
    @Column(name = "is_service")
    private Integer isService;


    // 购买人上传证据图片以及描述
    @Column(name = "buy_address")
    private String buyAddress;
    @Column(name = "buy_description")
    private String buyDescription;

    // 商品发布人上传证据以及描述
    @Column(name = "publish_address")
    private String publishAddress;
    @Column(name = "publish_description")
    private String publishDescription;

    // 提交申请反驳信用积分时间
    @Column(name = "create_time")
    private Date createTime;

    private Integer status;



}
