package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.util.List;
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

    /**
     * 修改一个用户的密码
     * @param user
     * @return
     */
    public boolean updataUser(User user);


    /**
     * 查询所有后台管理人员
     * @param page
     * @param limit
     * @return
     */
    public Page<User> getAdmins(Integer page, Integer limit);

    /**
     * 删除一个管理员用户
     * @param id
     * @return
     */
    public void deleteUser(Integer id);

    /**
     * 查询今天系统注册的人数
     * @return
     */
    public List<User> todaySign();


    /**
     * 这是查询发布最多二手商品的用户
     * @return
     */
    public List<Integer> maxPublishs();

    /**
     * 这是查询交易最多二手商品的用户
     * @return
     */
    public List<Integer> maxTrades();




}
