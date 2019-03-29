package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: chenxihua
 * @Date: 2018-11-16:16:59
 */
@Service
public class SchoolSericeImpl implements SchoolService {

    @Autowired
    SchoolDao schoolDao;

    /**
     * 方法用于学生申请学校（将新申请的学校信息设置为status=1）
     * @param school
     * @return
     */
    @Override
    public boolean saveSchoolInfo(School school) {
        school.setStatus(1);
        school.setApplyTime(new Date());
        School save = schoolDao.save(school);
        if(save != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 方法用于查询审核通过的所有学校
     * @return
     */
    @Override
    public List<School> findAllSchoolInfos() {
        School school = new School();
        school.setStatus(2);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<School> example = Example.of(school, matcher);
        List<School> schoolDaoAll = schoolDao.findAll(example);
        return schoolDaoAll;
    }

    /**
     * 根据学校id，查询这个学校信息
     * @param schId
     * @return
     */
    @Override
    public School getSchoolById(Integer schId) {
        Optional<School> school = schoolDao.findById(schId);
        return school.orElse(null);
    }

    /**
     * 查询出所有学生请求的学校，status=1，的学校
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<School> getSchoolChecked(Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "applyTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        School school = new School();
        school.setStatus(1);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<School> example = Example.of(school, matcher);
        Page<School> schools = schoolDao.findAll(example, pageable);

        return schools;
    }

    /**
     * 更新学校状态
     * @param school
     * @return
     */
    public boolean updateSchoolStatus(School school){
        School flush = schoolDao.saveAndFlush(school);
        if (flush!=null){
            return true;
        }else{
            return false;
        }
    }

}
