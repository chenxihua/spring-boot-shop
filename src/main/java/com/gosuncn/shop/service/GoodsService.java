package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:11:01
 *
 * 此类是用于商品操作
 *
 */
public interface GoodsService {


    /**
     * 用于保存商品信息，（即保存用户发布的商品信息）
     * @param goods
     * @return
     */
    public boolean saveGoodInfo(Goods goods);

    /** (一)
     * 根据学校id，分类查询发布在某学校的所有商品
     * @param schoolId
     * @return
     */
    //public Page<Goods> getGoodsBySchoolId(Integer schoolId, String typeId, Integer page, Integer limit);

    /** (二)
     * 和上面的查询方法一样，只是没有分页，而是把所有数据都查询出来
     * @param schoolId
     * @param typeId
     * @return
     */
    public List<Goods> getGoodsBySchoolId2(Integer schoolId, String typeId);

    /**
     * 根据商品id，得到一个商品信息
     * @param id
     * @return
     */
    public Goods getGoodsInfoById(Integer id);

    /**
     * 根据一个商品的id集合，得到一系列商品信息，用于收藏列表
     * @return
     */
    public List<Goods> getGoodsInfo(List<Integer> ids);

    /**
     * 用于更新一个商品的status，用于下订，以及下订成功
     * @param goods
     * @return
     */
    public boolean updateGoodsForStatus(Goods goods);

    /**
     * 这个方法，是用于查询一个用户，自己发布的所有二手商品。
     * @param userId
     * @return
     */
    public Page<Goods> findGoods(Integer userId, Integer page, Integer limit);

    /**
     * 根据id， 删除某一个以发布的商品
     * @param id
     * @return
     */
    public void deleteGoods(Integer id);

    /**
     * 查询所有等待审核的商品
     * @param page
     * @param limit
     * @return
     */
    public Page<Goods> findWaitCheck(Integer page, Integer limit);

    /**
     * 查询所有完成购买的商品总数
     * @return
     */
    public List<Goods> finishBuy();

    /**
     * 用于管理员，查询所有商品，分类的占比（用于饼状图与及折线图）
     * @return
     */
    public long goodTypes(Integer typeId);

    /**
     * 查询商品某一状态的数量
     * @param status
     * @return
     */
    public long goodStatus(Integer status);

    /**
     * 发布二手商品最多的学校
     * @return
     */
    public List<Integer> maxPublicSchool();

    /**
     * 交易二手商品最多的学校
     * @return
     */
    public List<Integer> maxTradeSchool();

    /**
     * 查询购买用户的性别，用于显示占比
     * @return
     */
    public List<User> buySexUser();

    /**
     * 查询发布用户的性别，用于显示发布占比
     * @return
     */
    public List<User> publishSexUser();

    public List<ShowDatas> publishCnSchool();

}
