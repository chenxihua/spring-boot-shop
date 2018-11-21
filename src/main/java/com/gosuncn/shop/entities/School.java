package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:13:52
 */
@Data
@Table(name = "school")
@Entity
public class School implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "area_id")
    private Integer areaId;
    private String address;
    private String phone;
    private String fax;

    @Column(name = "apply_time")
    private Date applyTime;
    private String description;
    private Integer status;


}
