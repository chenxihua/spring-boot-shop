package com.gosuncn.shop.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * @author: chenxihua
 * @Date: 2018-11-13:17:52
 */

@RequiredArgsConstructor
@Data
@Table(name = "user")
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "real_name")
    private String realName;
    private String account;
    private String password;
    private String salt;
    @Column(name = "school_id")
    private Integer schoolId;
    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private Integer sex;
    private String address;
    private Integer type;
    @Column(name = "sign_time")
    private Date signTime;
    private String signature;
    private Integer status;



}
