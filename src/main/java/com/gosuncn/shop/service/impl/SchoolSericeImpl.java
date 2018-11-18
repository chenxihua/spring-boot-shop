package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-11-16:16:59
 */
@Service
public class SchoolSericeImpl implements SchoolService {

    @Autowired
    SchoolDao schoolDao;

    @Override
    public boolean saveSchoolInfo(School school) {
        School save = schoolDao.save(school);
        if (StringUtils.isEmpty(save)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<School> findAllSchoolInfos() {
        return schoolDao.findAll();
    }
}
