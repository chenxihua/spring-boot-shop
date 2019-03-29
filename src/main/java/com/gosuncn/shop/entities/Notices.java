package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: chenxihua
 * @Date: 2019/2/22:9:10
 * @Version 1.0
 * 这个类是一个公告类，用于管理员之间的通知
 **/
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "notices")
@Entity
public class Notices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(name = "user_id")
    private Integer userId;
    private Date createTime;
    private String contents;
    private String fileUrl;
    private Integer status;

}
