package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:15:07
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@Table(name = "partion")
@Entity
public class Partion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer status;

}
