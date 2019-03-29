package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:54
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "comments")
@Entity
public class Comments implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 服务等级，*，**，***，****，*****；
    private Integer compliance;
    @Column(name = "publish_man")
    private Integer publishMan;

    @Column(name = "orders_id")
    private Integer ordersId;

    @Column(name = "user_id")
    private Integer userId;
    private String description;

    @Column(name = "start_time")
    private Date startTime;

    private Integer status;


}
