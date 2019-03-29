package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.Items;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:9:17
 */
public interface ItemsService {


    /**
     * 查询所有商品分类
     * @return
     */
    public List<Items> getItemsByAll();

}
