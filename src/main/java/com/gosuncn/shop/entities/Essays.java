package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:14:59
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
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

    @Column(name = "publish_time")
    private Date publishTime;
    private Integer loves;
    private Integer hates;
    private Integer looks;
    private Integer status;


}
