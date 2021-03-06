package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.School;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-11-16:16:58
 */
public interface SchoolService {

    public boolean saveSchoolInfo(School school);

    public List<School> findAllSchoolInfos();

    public School getSchoolById(Integer schId);

    public Page<School> getSchoolChecked(Integer page, Integer limit);

    public boolean updateSchoolStatus(School school);
}
