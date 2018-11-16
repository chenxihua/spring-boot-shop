package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:10:48
 */

@Data
@Table(name = "administrator")
@Entity
public class Administrator implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "real_name")
    private String realName;
    private String account;
    private String password;
    @Column(name = "role_id")
    private Integer roleId;
    private String salt;
    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private Integer sex;
    private String address;
    @Column(name = "add_time")
    private Date addTime;
    @Column(name = "cancel_time")
    private Date cancelTime;
    private String signature;
    private Integer status;

}
