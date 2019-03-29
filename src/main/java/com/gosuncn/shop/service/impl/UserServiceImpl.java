package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.GoodsDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Collection;
import com.gosuncn.shop.entities.Goods;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: chenxihua
 * @Date: 2018-11-21:9:58
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    GoodsDao goodsDao;

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

    /**
     * 修改一个用户的密码
     * @param user
     * @return
     */
    @Override
    public boolean updataUser(User user) {
        User saveAndFlush = userDao.saveAndFlush(user);
        if (saveAndFlush!=null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询所有后台管理人员
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<User> getAdmins(Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        User user = new User();
        user.setType(4);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<User> example = Example.of(user, matcher);

        Page<User> users = userDao.findAll(example, pageable);
        return users;
    }

    /**
     * 删除一个管理员用户
     * @param id
     * @return
     */
    @Override
    public void deleteUser(Integer id) {
        userDao.deleteById(id);
    }


    /**
     * 查询今天系统注册的人数
     * @return
     */
    @Override
    public List<User> todaySign() {
        // 创建查询规格对象
        Specification<User> specification = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date zero1 = calendar.getTime();

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            Date zero2 = c.getTime();

            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(cb.between(root.get("signTime").as(Date.class), zero1, zero2));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        List<User> userList = userDao.findAll(specification);

        return userList;
    }

    /**
     * 这是查询发布最多二手商品的用户
     * @return
     */
    @Override
    public List<Integer> maxPublishs() {
        List<Integer> ids = new ArrayList<>();
        List<Long> nums = new ArrayList<>();
        Map<Integer, Long> map = new HashMap<>();
        List<User> users = userDao.findAll();
        try {
            for (User user : users) {
                Goods goods = new Goods();
                goods.setPublishMan(user.getId());
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                        .withIgnoreNullValues();
                Example<Goods> example = Example.of(goods, matcher);
                long counts = goodsDao.count(example);   // 这种情况，有可能出现交易总数一样的学校，

                map.put(user.getId(), counts);
                nums.add(counts);
            }

            Long max = Collections.max(nums);

            for(User user : users){
                Long num = map.get(user.getId());
                if (max.equals(num)){
                    ids.add(user.getId());
                }
            }
            return ids;
        }catch (Exception e){
            e.printStackTrace();
            log.info("查询发布最多二手商品的用户出现严重错误，错误原因为："+e);
        }

        return ids;
    }

    /**
     * 这是查询交易最多二手商品的用户
     * @return
     */
    @Override
    public List<Integer> maxTrades() {
        List<Integer> ids = new ArrayList<>();
        List<Long> nums = new ArrayList<>();
        Map<Integer, Long> map = new HashMap<>();
        List<User> users = userDao.findAll();
        try {
            for (User user : users) {
                Goods goods = new Goods();
                goods.setFinishMan(user.getId());
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                        .withIgnoreNullValues();
                Example<Goods> example = Example.of(goods, matcher);
                long counts = goodsDao.count(example);   // 这种情况，有可能出现交易总数一样的学校，

                map.put(user.getId(), counts);
                nums.add(counts);
            }

            Long max = Collections.max(nums);

            for(User user : users){
                Long num = map.get(user.getId());
                if (max.equals(num)){
                    ids.add(user.getId());
                }
            }
            return ids;
        }catch (Exception e){
            e.printStackTrace();
            log.info("查询交易最多二手商品的用户出现严重错误，错误原因为："+e);
        }

        return ids;
    }


}
