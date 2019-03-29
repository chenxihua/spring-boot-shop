package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenxihua
 * @Date: 2019/2/27:11:20
 * @Version 1.0
 **/

@Slf4j
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
public class SysTotals {

    private Integer id;
    private Long maxNums;
}
