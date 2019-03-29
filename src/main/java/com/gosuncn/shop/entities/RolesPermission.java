package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:24
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "roles_permission")
@Entity
public class RolesPermission implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "permission_id")
    private Integer permissionId;
    @Column(name = "roles_id")
    private Integer rolesId;
    private Integer status;
}
