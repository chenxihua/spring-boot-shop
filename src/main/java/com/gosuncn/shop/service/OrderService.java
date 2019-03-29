package com.gosuncn.shop.service;

import com.gosuncn.shop.entities.Orders;
import org.springframework.data.domain.Page;

/**
 * @author: chenxihua
 * @Date: 2019-01-10:17:00
 * 这个接口，主要是对订单逻辑代码进行设计的
 */
public interface OrderService {


    /**
     * 保存订单
     * @param orders
     * @return
     */
    public boolean saveOrder(Orders orders);

    /**
     * 查询所有订单
     * @param uId
     * @param page
     * @param limit
     * @return
     */
    public Page<Orders> findOrders(Integer uId, Integer page, Integer limit);

    /**
     * 更改订单状态
     * @param orders
     * @return
     */
    public boolean updateOrdersStatus(Orders orders, Integer status);

    /**
     * 根据id，找到某一个orders
     * @param id
     * @return
     */
    public Orders findOrderById(Integer id);

    /**
     * 根据id，删除某一个订单
     * @param id
     */
    public void deleteOrderById(Integer id);

    /**
     * 在订单表里面，根据商品id，查找某一个订单（与根据id，查找订单不一样）；
     * @param id
     * @return
     */
    public Orders findOrderByGoodsId(Integer id, Integer status);

}
