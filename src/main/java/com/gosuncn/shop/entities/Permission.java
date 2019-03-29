package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:16
 * 这是权限表
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "permission")
@Entity
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 上级id,
    private Integer pid;
    // 权限名称
    private String name;
    private String description;
    private String url;
    private String code;
    private Integer status;

}
