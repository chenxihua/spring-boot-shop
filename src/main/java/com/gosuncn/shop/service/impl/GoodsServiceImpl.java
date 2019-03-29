package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.*;
import com.gosuncn.shop.entities.*;
import com.gosuncn.shop.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:11:03
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ItemsDao itemsDao;
    @Autowired
    SchoolDao schoolDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ProvinceDao provinceDao;
    @Autowired
    PartionDao partionDao;

    /**
     * 用于保存用户发布的商品信息
     * @return
     */
    @Override
    public boolean saveGoodInfo(Goods goods) {
        Goods saveGood = goodsDao.save(goods);
        if(saveGood != null){
            return true;
        }else {
            return false;
        }
    }

    /***
     * 根据学校id，以及商品类型id，分类查询发布在某一学校的所有商品
     * @param schoolId
     * @return
     */
//    @Override
//    public Page<Goods> getGoodsBySchoolId(Integer schoolId, String typeId, Integer page, Integer limit) {
//
//        Sort sort = new Sort(Sort.Direction.DESC, "publishTime");
//        Pageable pageable = PageRequest.of(page-1, limit, sort);
//
//        Goods goods = new Goods();
//        if (schoolId!=null){
//            goods.setSchoolId(schoolId);
//        }
//        if (typeId!=null&&typeId!=""){
//            goods.setTypeId(typeId);
//        }
//        goods.setStatus(2);
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
//        Example<Goods> exampl = Example.of(goods, matcher);
//
//        Page<Goods> goodsPage = goodsDao.findAll(exampl, pageable);
//
//        return goodsPage;
//    }

    @Override
    public List<Goods> getGoodsBySchoolId2(Integer schoolId, String typeId) {
        Sort sort = new Sort(Sort.Direction.DESC, "publishTime");
        Goods goods = new Goods();
        goods.setSchoolId(schoolId);
        if (typeId!=null&&typeId!=""){
            goods.setTypeId(typeId);
        }
        goods.setStatus(2);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Goods> exampl = Example.of(goods, matcher);
        List<Goods> all = goodsDao.findAll(exampl, sort);

        return all;
    }

    /**
     * 根据商品id，得到一个商品信息
     * @param id
     * @return
     */
    @Override
    public Goods getGoodsInfoById(Integer id) {
        Goods daoOne = goodsDao.getOne(id);
        return daoOne;
    }

    /**
     * 这个方法是由商品id集合，得到一个商品信息集合，用于商品收藏列表
     * 这里传入的是商品id集
     * @param ids
     * @return
     */
    @Override
    public List<Goods> getGoodsInfo(List<Integer> ids) {
        List<Goods> goodByIds = goodsDao.findAllById(ids);
        return goodByIds;
    }

    /**
     * 用于更新一个商品的status，用于下订，以及下订成功
     * @param goods
     * @return
     */
    @Override
    public boolean updateGoodsForStatus(Goods goods) {
        Goods andFlush = goodsDao.saveAndFlush(goods);
        if (andFlush!=null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 这个方法，是用于查询一个用户，自己发布的所有二手商品。
     * @param userId
     * @return
     */
    @Override
    public Page<Goods> findGoods(Integer userId, Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "publishTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Goods goods = new Goods();
        goods.setPublishMan(userId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Goods> example = Example.of(goods, matcher);

        Page<Goods> goodsDaoAll = goodsDao.findAll(example, pageable);

        return goodsDaoAll;
    }

    /**
     * 根据id， 删除某一个以发布的商品
     * @param id
     */
    @Override
    public void deleteGoods(Integer id) {
        goodsDao.deleteById(id);
    }

    /**
     * 查询所有等待审核的商品
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<Goods> findWaitCheck(Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.ASC, "publishTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Goods goods = new Goods();
        goods.setStatus(1);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Goods> example = Example.of(goods, matcher);
        Page<Goods> all = goodsDao.findAll(example, pageable);

        return all;
    }

    /**
     * 查询所有完成购买的商品总数, 用于管理员统计数据
     * @return
     */
    @Override
    public List<Goods> finishBuy() {
        Goods goods = new Goods();
        goods.setStatus(4);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Goods> example = Example.of(goods, matcher);
        List<Goods> goodsDaoAll = goodsDao.findAll(example);
        return goodsDaoAll;
    }

    /**
     * 用于管理员，查询所有商品，分类的占比（用于饼状图与及折线图）
     * @return
     */
    @Override
    public long goodTypes(Integer typeId) {
        Goods goods = new Goods();
        goods.setTypeId(String.valueOf(typeId));
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Goods> example = Example.of(goods, matcher);
        List<Goods> goodsDaoAll = goodsDao.findAll(example);
        return (long)goodsDaoAll.size();
    }

    /**
     * 查询商品某一状态的数量
     * @param status
     * @return
     */
    @Override
    public long goodStatus(Integer status) {
        Goods goods = new Goods();
        goods.setStatus(status);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Goods> example = Example.of(goods, matcher);
        List<Goods> goodsList = goodsDao.findAll(example);
        return (long)goodsList.size();
    }


    /**
     * 发布二手商品最多的学校
     * @return
     */
    @Override
    public List<Integer> maxPublicSchool() {
        List<Integer> ids = new ArrayList<>();
        List<Long> nums = new ArrayList<>();
        Map<Integer, Long> map = new HashMap<>();
        List<School> all = schoolDao.findAll();

        try {
            for (School school : all) {
                Goods goods = new Goods();
                goods.setSchoolId(school.getId());
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                        .withIgnoreNullValues();
                Example<Goods> example = Example.of(goods, matcher);
                long count = goodsDao.count(example);   // 这种情况，有可能出现发布总数一样的学校，

                map.put(school.getId(), count);
                nums.add(count);
            }
            Long max = Collections.max(nums);

            for(School sch : all){
                Long num = map.get(sch.getId());
                if (max.equals(num)){
                    ids.add(sch.getId());
                }
            }

            return ids;
        }catch (Exception e){
            e.printStackTrace();
            log.error("出现严重错误；错误信息为："+e);
        }
        return ids;
    }

    /**
     * 交易二手商品最多的学校
     * @return
     */
    @Override
    public List<Integer> maxTradeSchool() {
        List<Integer> ids = new ArrayList<>();
        List<Long> nums = new ArrayList<>();
        Map<Integer, Long> map = new HashMap<>();
        List<School> all = schoolDao.findAll();
        try {
            for (School school : all) {
                Goods goods = new Goods();
                goods.setSchoolId(school.getId());
                goods.setStatus(4);
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                        .withIgnoreNullValues();
                Example<Goods> example = Example.of(goods, matcher);
                long count = goodsDao.count(example);   // 这种情况，有可能出现交易总数一样的学校，

                map.put(school.getId(), count);
                nums.add(count);
            }
            Long max = Collections.max(nums);

            for(School sch : all){
                Long num = map.get(sch.getId());
                if (max.equals(num)){
                    ids.add(sch.getId());
                }
            }

            return ids;
        }catch (Exception e){
            e.printStackTrace();
            log.error("出现严重错误；错误信息为："+e);
        }
        return ids;
    }

    /**
     * 查询购买用户的性别，用于显示占比
     * @return
     */
    @Override
    public List<User> buySexUser() {
        List<User> userList = new ArrayList<>();

        Goods goods = new Goods();
        goods.setStatus(4);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<Goods> example = Example.of(goods, matcher);
        List<Goods> goodsList = goodsDao.findAll(example);
        for (Goods good : goodsList) {
            Integer userId = good.getFinishMan();
            User one = userDao.getOne(userId);
            userList.add(one);
        }
        return userList;
    }

    /**
     * 查询购买用户的性别，用于显示占比
     * @return
     */
    @Override
    public List<User> publishSexUser() {
        List<User> userList = new ArrayList<>();

        List<Goods> goodsList = goodsDao.findAll();
        for (Goods good : goodsList) {
            Integer userId = good.getPublishMan();
            User one = userDao.getOne(userId);
            userList.add(one);
        }
        return userList;
    }

    /**
     * 查询发布大区占比
     * @return
     */
    @Override
    public List<ShowDatas> publishCnSchool() {
        List<ShowDatas> showDatasList = new ArrayList<>();

        Integer eastArea = 0;  Integer southArea = 0;  Integer centerArea = 0;  Integer northArea = 0;
        Integer westNorthArea = 0;  Integer westSouthArea = 0;  Integer eastNorthArea = 0;  Integer specialArea = 0;

        List<Goods> goodsList = goodsDao.findAll();
        for (Goods good : goodsList) {
            School school = schoolDao.getOne(good.getSchoolId());
            Province province = provinceDao.getOne(school.getAreaId());
            Partion partion = partionDao.getOne(province.getPartionId());
            Integer areaId = partion.getId();
            if (areaId==1){
                eastArea++;
            }else if(areaId==2){
                southArea++;
            }else if(areaId==3){
                centerArea++;
            }else if(areaId==4){
                northArea++;
            }else if(areaId==5){
                westNorthArea++;
            }else if(areaId==6){
                westSouthArea++;
            }else if(areaId==7){
                eastNorthArea++;
            }else if(areaId==8){
                specialArea++;
            }
        }

        List<Partion> partionList = partionDao.findAll();
        for (Partion partion : partionList) {
            ShowDatas showDatas = new ShowDatas();
            Integer parId = partion.getId();
            if (parId==1){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(eastArea);
            }else if (parId==2){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(southArea);
            }else if (parId==3){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(centerArea);
            }else if (parId==4){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(northArea);
            }else if (parId==5){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(westNorthArea);
            }else if (parId==6){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(westSouthArea);
            }else if (parId==7){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(eastNorthArea);
            }else if (parId==8){
                showDatas.setKey(partion.getName());
                showDatas.setNumData(specialArea);
            }
            showDatasList.add(showDatas);
        }

        return showDatasList;
    }
}
