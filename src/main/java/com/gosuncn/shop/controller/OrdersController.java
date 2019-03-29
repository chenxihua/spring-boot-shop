package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.OrdersDao;
import com.gosuncn.shop.entities.Collection;
import com.gosuncn.shop.entities.Goods;
import com.gosuncn.shop.entities.Orders;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.GoodsService;
import com.gosuncn.shop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: chenxihua
 * @Date: 2019-01-10:17:25
 */
@Slf4j
@RequestMapping(value = "/orders")
@Controller
public class OrdersController {

    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrdersDao ordersDao;

    /**
     * 保存一个订单，这里如果下订的话，那就得将商品status，改为下订状态。
     * @param session
     * @param orders
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveOrdersByNew")
    public Map<String, Object> saveOrders(HttpSession session, Orders orders){
        Map<String,Object> map = new HashMap<>();
        User currentUser = (User)session.getAttribute("currentUser");
        orders.setUserId(currentUser.getId());
        orders.setOrderUuid(String.valueOf(System.currentTimeMillis()));
        orders.setStartTime(new Date());
        orders.setStatus(1);

        boolean ordersFlag = orderService.saveOrder(orders);

        // 保存商品Goods的状态，改下正在交易
        Goods goodsInfoById = goodsService.getGoodsInfoById(orders.getGoodsId());
        goodsInfoById.setStatus(3);
        boolean statusBlags = goodsService.updateGoodsForStatus(goodsInfoById);

        if (ordersFlag&&statusBlags){
            map.put("code", 0);
            map.put("success", true);
            map.put("msg", "保存成功");
        }else {
            map.put("code", 1);
            map.put("success", false);
            map.put("msg", "保存失败");
        }
        return map;
    }


    /**
     * 更新订单状态，取消订单
     * @param id
     * @return
     */
    @ResponseBody
    @PutMapping(value = "/updateOrders")
    public Integer updateOrders(@RequestParam("id")Integer id){
        try {
            Orders order = orderService.findOrderById(id);

            boolean updateStatus = orderService.updateOrdersStatus(order, 0);

            // 保存商品Goods的状态，改下成功通过发布，正在出售
            Goods goodsInfoById = goodsService.getGoodsInfoById(order.getGoodsId());
            goodsInfoById.setStatus(2);
            goodsService.updateGoodsForStatus(goodsInfoById);

            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新订单状态，订单交易成功。
     * @param id
     * @return
     */
    @ResponseBody
    @PutMapping(value = "/updateSuccess")
    public Integer updateSuccess(@RequestParam("id")Integer id){
        try {
            Orders order = orderService.findOrderById(id);

            boolean updateSuccess = orderService.updateOrdersStatus(order, 2);

            // 保存商品Goods的状态，改下交易成功，已经下架
            Goods goodsInfoById = goodsService.getGoodsInfoById(order.getGoodsId());
            goodsInfoById.setFinishTime(new Date());
            // 用于保存商品的最终购买人
            goodsInfoById.setFinishMan(order.getUserId());
            goodsInfoById.setStatus(4);

            goodsService.updateGoodsForStatus(goodsInfoById);

            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除某一个订单
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping(value = "/deleteOrders/{id}")
    public Integer deleteOrders(@PathVariable("id")Integer id){
        try {
            orderService.deleteOrderById(id);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    //   ----------------------         分割线         ----------------------------

    @ResponseBody
    @RequestMapping(value = "/orders_list")
    public Map<String, Object> ordersList(HttpSession session, Integer page, Integer limit, String startTime, String endTime){
        Map<String, Object> result = new HashMap<>();
        try{
            User currentUser = (User)session.getAttribute("currentUser");
//            Integer page = Integer.parseInt(queryParams.get("page").toString());
//            Integer limit = Integer.parseInt(queryParams.get("limit").toString());

            // 创建查询规格对象
            Specification<Orders> specification = (Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d1 = new Date();
                Date d2 = new Date();
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
                    try {
                        d1=sdf.parse(startTime);
                        d2=sdf.parse(endTime);
                    } catch (ParseException ee) {
                        ee.printStackTrace();
                        log.error("时间格式转换出错了，具体为："+ee);
                    }
                    predicates.add(cb.between(root.get("startTime").as(Date.class), d1, d2));
                }
                predicates.add(cb.equal(root.get("userId").as(Integer.class), currentUser.getId()));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "startTime");
            Page<Orders> orders = ordersDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", orders.getTotalElements());
            result.put("data", orders.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，查询个人订单记录错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }

}
