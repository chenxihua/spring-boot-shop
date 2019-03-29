package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:13:52
 */

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Entity
@Table(name = "sysLog")
public class SysLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 用户名
    private String username;
    // 操作
    private String operation;
    // 方法名
    private String method;
    // 参数
    private String params;
    // ip地址
    private String ip;
    // 访问操作的URL；
    private String uri;
    // 操作时间
    private Date createDate;
}
