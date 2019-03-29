package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:26
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "goods")
@Entity
public class Goods implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String goodCode;
    private String name;
    @Column(name = "original_price")
    private Double originalPrice;
    private Double price;
    private Integer available;
    @Column(name = "publish_man")
    private Integer publishMan;
    @Column(name = "school_id")
    private Integer schoolId;
    private String phone;
    private String wechat;
    private String description;
    @Column(name = "one_address")
    private String oneAddress;

    @Column(name = "publish_time")
    private Date publishTime;
    @Column(name = "finish_time")
    private Date finishTime;
    @Column(name = "finish_man")
    private Integer finishMan;

    @Column(name = "type_id")
    private String typeId;
    @Column(name = "comment_id")
    private Integer commentId;
    private Integer status;



}




