package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:14:51
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
@Data
@Table(name = "roles")
@Entity
public class Roles implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String descript;
    private Integer status;
}
