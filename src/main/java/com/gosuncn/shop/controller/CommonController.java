package com.gosuncn.shop.controller;

/**
 * @author: chenxihua
 * @Date: 2018-11-21:8:46
 * 这个controoler是直接做页面跳转的，其它的Controller就做成 @Restcontroller
 */

import com.gosuncn.shop.dao.GoodsDao;
import com.gosuncn.shop.dao.RolesDao;
import com.gosuncn.shop.entities.*;
import com.gosuncn.shop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/common")
public class CommonController {


    @Autowired
    SchoolService schoolService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    CollectionService collectionService;
    @Autowired
    OrderService orderService;
    @Autowired
    ItemsService itemsService;
    @Autowired
    RolesService rolesService;
    @Autowired
    RolesDao rolesDao;

    @RequestMapping(value = "/register")
    public String register(){
        return "user/register";
    }

    @RequestMapping(value = "/applySchool")
    public String upSchool(){
        return "user/schools";
    }

    @RequestMapping(value = "/loginIndex")
    public String loginSuccess(Model model){

        // 创建查询规格对象
        Specification<Goods> specification = (Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(cb.equal(root.get("status").as(Integer.class), 2));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "publishTime");
        Page<Goods> goods = goodsDao.findAll(specification, pageable);
        List<Goods> goodsContent = goods.getContent();
        int size = goodsContent.size();

        if (size==1){
            model.addAttribute("onePhoto", goodsContent.get(0).getOneAddress());
        }else if (size==2){
            model.addAttribute("onePhoto", goodsContent.get(0).getOneAddress());
            model.addAttribute("twoPhoto", goodsContent.get(1).getOneAddress());
        }else if(size==3){
            model.addAttribute("onePhoto", goodsContent.get(0).getOneAddress());
            model.addAttribute("twoPhoto", goodsContent.get(1).getOneAddress());
            model.addAttribute("threePhoto", goodsContent.get(2).getOneAddress());
        }else if(size==4){
            model.addAttribute("onePhoto", goodsContent.get(0).getOneAddress());
            model.addAttribute("twoPhoto", goodsContent.get(1).getOneAddress());
            model.addAttribute("threePhoto", goodsContent.get(2).getOneAddress());
            model.addAttribute("fourPhoto", goodsContent.get(3).getOneAddress());
        }else if(size==5){
            model.addAttribute("onePhoto", goodsContent.get(0).getOneAddress());
            model.addAttribute("twoPhoto", goodsContent.get(1).getOneAddress());
            model.addAttribute("threePhoto", goodsContent.get(2).getOneAddress());
            model.addAttribute("fourPhoto", goodsContent.get(3).getOneAddress());
            model.addAttribute("fivePhoto", goodsContent.get(4).getOneAddress());
        }
        return "pages/userLogin";
    }

    /**
     * 去到商品查询页面
     * @return
     */
    @RequestMapping(value = "/lookForGoods")
    public String searchAllGoods(){
//        List<School> schoolInfos = schoolService.findAllSchoolInfos();
//        List<Items> itemsByAll = itemsService.getItemsByAll();
//        model.addAttribute("schoolInfos", schoolInfos);
//        model.addAttribute("items", itemsByAll);
        return "goods/lookGoods";
    }

    @RequestMapping(value = "/releaseGoods")
    public String releaseUpGood(){
        return "goods/upGoods";
    }

    /**
     * 这个是在查找二手商品时，点击进去进行购买或者收藏的页面
     * @return
     */
    @RequestMapping(value = "/getGoodInfo/{paraUrl}")
    public String getGoodInfoById(@PathVariable("paraUrl")Integer paraUrl, Model model){
        Goods infos = goodsService.getGoodsInfoById(paraUrl);
        model.addAttribute("goodInfo", infos);
        return "goods/goodInfos";
    }

    /**
     * 这个与上面的不同，这个是用于在已经购买的订单处，点击进行查询购买的详细信息
     * @return
     */
    @RequestMapping(value = "/getOrdersGoodsInfos/{paraUrl}")
    public String getGoodsInfos(@PathVariable("paraUrl")Integer paraUrl, Model model){
        Goods infos = goodsService.getGoodsInfoById(paraUrl);
        model.addAttribute("goodsInfos", infos);
        return "goods/findGoodsInfos";
    }

    @RequestMapping(value = "/discuss/{orderId}")
    public String getDiscussInfo(@PathVariable("orderId")Integer orderId, Model model){
        Orders orders = orderService.findOrderById(orderId);
        Goods infos = goodsService.getGoodsInfoById(orders.getGoodsId());
        model.addAttribute("goodInfos", infos);
        model.addAttribute("orders", orders);
        return "goods/discussOrders";
    }

    @RequestMapping(value = "/collectGoods")
    public String getCollectList(){
        return "goods/collectList";
    }

    @RequestMapping(value = "/buyGoods")
    public String getBuyList(){
        return "goods/buyList";
    }


    @RequestMapping(value = "/saveOrderById/{goodsId}")
    public String getOrderPage(@PathVariable("goodsId")Integer goodIdVal, Model model){
        model.addAttribute("goodValId", goodIdVal);
        return "goods/getOrder";
    }

    @RequestMapping(value = "/userInfos")
    public String getUserInfoPage(){
        return "user/userInfos";
    }

    @RequestMapping(value = "/updatePass")
    public String getChangePass(){
        return "user/changePassword";
    }


    @RequestMapping(value = "/trading")
    public String getTradingPage(){
        return "goods/publishList";
    }


    @RequestMapping(value = "/addComments/{ordersId}")
    public String getToCommentsPage(@PathVariable("ordersId")Integer orderId, Model model){
        Orders orders = orderService.findOrderById(orderId);
        Goods infos = goodsService.getGoodsInfoById(orders.getGoodsId());
        model.addAttribute("orders", orders);
        model.addAttribute("goods", infos);
        return "ordersRecord/addComments";
    }


    @RequestMapping(value = "/getTradeInfos/{goodsId}")
    public String getTradeInfoPage(@PathVariable("goodsId")Integer goodsId, Model model){
        Orders orders = orderService.findOrderByGoodsId(goodsId,1);
        model.addAttribute("orders", orders);
        return "ordersRecord/tradeDescribe";
    }

    @RequestMapping(value = "/getSellerComments/{goodsId}")
    public String getSellerDiscuss(@PathVariable("goodsId")Integer goodsId, Model model){
        Orders orderByGoodsId = orderService.findOrderByGoodsId(goodsId, 2);
        model.addAttribute("orders", orderByGoodsId);
        return "ordersRecord/getOrdersComments";
    }

    @RequestMapping(value = "/verifyEmail")
    public String getTrueEmail(){
        return "user/verifyUser";
    }


    /**
     * 管理员登录的页面
     */
    @RequestMapping(value = "/admin")
    public String getAdminPage(){
        return "admin/login";
    }


    /**
     * -----------------   过年更新代码   ----------------------
     */

    @RequestMapping(value = "/adminRoles")
    public String getRoles(){
        return "admin/rolesPages";
    }

    @RequestMapping(value = "/adminPermission")
    public String getPermission(){
        return "admin/permissionPage";
    }

    @RequestMapping(value = "/adminUsers")
    public String getAdminUser(){
        return "admin/adminPage";
    }



    @RequestMapping(value = "/getAdminUsers")
    public String getAddAdmins(){
        return "admin/addAdmins";
    }

    @RequestMapping(value = "/getAdminRoles/{id}")
    public String getAdminRoles(@PathVariable("id")Integer id, Model model){
        List<Roles> rolesList = rolesService.getAllRolesList();
        model.addAttribute("rolesList", rolesList);
        model.addAttribute("userId", id);
        return "admin/addRolesToAdmin";
    }

    @RequestMapping(value = "/checkSchools")
    public String getCheckSchools(){
        return "manager/checkSchools";
    }


    @RequestMapping(value = "/checkGoods")
    public String getGoodsRequest(){
        return "manager/checkGoods";
    }


    @RequestMapping(value = "/checkGoodInfo/{id}")
    public String getCheckGoods(@PathVariable("id")Integer id, Model model){
        Goods goods = goodsService.getGoodsInfoById(id);
        model.addAttribute("goodInfo", goods);
        return "manager/checkGoodsInfo";
    }


    //  ------------     以下为公告内容     ================

    @RequestMapping(value = "/NoticesManager")
    public String getNotices(){
        return "admin/notices";
    }

    @RequestMapping(value = "/searchLogs")
    public String getLogs(){
        return "admin/showLogs";
    }

    @RequestMapping(value = "/totalpublishs")
    public String totalPublishs(){
        return "admin/totals";
    }


    //  ----------       信用评分      ---------------------


    @RequestMapping(value = "/creditConflict")
    public String conflict(){
        return "admin/conflict";
    }

}
