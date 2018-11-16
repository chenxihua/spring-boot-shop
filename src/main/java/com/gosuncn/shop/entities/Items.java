package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:38
 */

@Data
@Table(name = "items")
@Entity
public class Items {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer status;

}
