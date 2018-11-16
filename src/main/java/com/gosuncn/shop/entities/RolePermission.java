package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:24
 */
@Data
@Table(name = "role_permission")
@Entity
public class RolePermission implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer perId;
    private Integer roleId;
    private Integer status;
}
