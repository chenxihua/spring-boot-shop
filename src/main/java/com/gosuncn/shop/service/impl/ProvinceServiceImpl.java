package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.ProvinceDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-11-20:16:16
 */

@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    ProvinceDao provinceDao;

    @Override
    public List<Province> getAllProvince() {
        return provinceDao.findAll();
    }
}
