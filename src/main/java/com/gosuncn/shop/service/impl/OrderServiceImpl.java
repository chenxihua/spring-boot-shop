package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.OrdersDao;
import com.gosuncn.shop.entities.Orders;
import com.gosuncn.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: chenxihua
 * @Date: 2019-01-10:17:07
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrdersDao ordersDao;

    /**
     * 保存一个订单实体
     * @param orders
     * @return
     */
    @Override
    public boolean saveOrder(Orders orders) {
        Orders saveOrder = ordersDao.save(orders);
        if (saveOrder != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询个人所有订单信息
     * @param uId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<Orders> findOrders(Integer uId, Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Orders orders = new Orders();
        orders.setUserId(uId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnorePaths("status");
        Example<Orders> example = Example.of(orders, matcher);

        Page<Orders> ordersPage = ordersDao.findAll(example, pageable);

        return ordersPage;
    }


    /**
     * 更改订单状态
     * @param orders
     * @return
     */
    @Override
    public boolean updateOrdersStatus(Orders orders, Integer status) {
        orders.setStatus(status);

        Orders save = ordersDao.saveAndFlush(orders);
        if (save!=null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据id，查找某一个订单id
     * @param id
     * @return
     */
    @Override
    public Orders findOrderById(Integer id) {
        Optional<Orders> byId = ordersDao.findById(id);
        return byId.orElse(null);
    }

    /**
     * 直接删除某个订单记录
     * @param id
     */
    @Override
    public void deleteOrderById(Integer id) {
        ordersDao.deleteById(id);
    }

    /**
     * 在订单表里面，根据商品id，查找某一个订单（与根据id，查找订单不一样）；
     * @param id
     * @return
     */
    @Override
    public Orders findOrderByGoodsId(Integer id, Integer status) {
        Orders orders = new Orders();
        orders.setGoodsId(id);
        orders.setStatus(status);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Orders> example = Example.of(orders, matcher);

        Optional<Orders> one = ordersDao.findOne(example);
        return one.orElse(null);
    }

}
