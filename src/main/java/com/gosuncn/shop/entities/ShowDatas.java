package com.gosuncn.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenxihua
 * @Date: 2019/2/26:10:58
 * @Version 1.0
 **/
@Slf4j
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
public class ShowDatas {

    String key;
    long numData;
}
