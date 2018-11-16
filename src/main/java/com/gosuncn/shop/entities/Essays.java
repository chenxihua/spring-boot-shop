package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:14:59
 */
@Data
@Table(name = "essay")
@Entity
public class Essays implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String anonymous;
    @Column(name = "user_id")
    private Integer userId;
    private String content;
    private Date time;
    private Integer loves;
    private Integer hates;
    private Integer looks;
    private Integer status;


}
