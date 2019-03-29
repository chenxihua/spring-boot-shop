package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.ItemsDao;
import com.gosuncn.shop.entities.Items;
import com.gosuncn.shop.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:9:19
 */
@Service
public class ItemsServiceImpl implements ItemsService {


    @Autowired
    ItemsDao itemsDao;

    /**
     * 查询所有商品分类
     * @return
     */
    @Override
    public List<Items> getItemsByAll() {
        List<Items> itemsList = itemsDao.findAll();
        return itemsList;
    }








}





