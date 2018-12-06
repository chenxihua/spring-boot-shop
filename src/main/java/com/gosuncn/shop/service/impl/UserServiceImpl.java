package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author: chenxihua
 * @Date: 2018-11-21:9:58
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    /**
     * 保存一个新注册用户
     * @return
     */
    @Override
    public boolean saveOneUser(User user) {
        User saveOne = userDao.save(user);
        if(saveOne != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据邮箱，或者手机号，或昵称，查找用户是否存在
     * @param example
     * @return
     */
    @Override
    public boolean findByConditions(Example<User> example) {

        return userDao.exists(example);
    }

    /**
     * 根据条件来查询一个用户，得到这个用户的信息
     * @param conditions
     * @return
     */
    @Override
    public User findByConditionsToUser(String conditions) {

        // 这里是根据邮箱，进行查询
        User user = new User();
        user.setEmail(conditions);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreCase(false)
                .withIgnoreNullValues();
        Example<User> example = Example.of(user, matcher);
        Optional<User> userDaoOne = userDao.findOne(example);

        return userDaoOne.orElse(null);
    }
}
