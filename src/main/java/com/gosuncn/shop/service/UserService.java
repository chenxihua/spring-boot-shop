package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.User;
import org.springframework.data.domain.Example;

import java.util.Optional;

/**
 * @author: chenxihua
 * @Date: 2018-11-21:9:57
 */
public interface UserService {

    /**
     * 注册一个用户
     * @return
     */
    public boolean saveOneUser(User user);

    /**
     * 根据邮箱，或者手机号，或昵称，查找用户是否存在
     * @param example
     * @return
     */
    public boolean findByConditions(Example<User> example);

    /**
     * 根据条件来查询一个用户，得到这个用户的信息
     * @param conditions
     * @return
     */
    public User findByConditionsToUser(String conditions);
}
