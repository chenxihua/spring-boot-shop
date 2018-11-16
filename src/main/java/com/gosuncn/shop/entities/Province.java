package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:14:11
 */
@Data
@Table(name = "provinces")
@Entity
public class Province implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(name = "partion_id")
    private Integer partionId;
    private Integer status;

}
