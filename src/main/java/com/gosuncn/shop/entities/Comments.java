package com.gosuncn.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:11:54
 */
@Data
@Table(name = "comments")
@Entity
public class Comments implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer compliance;
    @Column(name = "provide_service")
    private Integer provideService;
    @Column(name = "publish_man")
    private Integer publishMan;

    @Column(name = "user_id")
    private Integer userId;
    private String description;

    @Column(name = "comment_time")
    private Date commentTime;
    @Column(name = "again_description")
    private String againDescription;
    @Column(name = "again_time")
    private Date againTime;
    private Integer status;


}
