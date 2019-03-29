package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.GoodsDao;
import com.gosuncn.shop.dao.OrdersDao;
import com.gosuncn.shop.dao.UserCreditDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.*;
import com.gosuncn.shop.service.UserCreditService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: chenxihua
 * @Date: 2019/3/10:17:16
 * @Version 1.0
 *
 * 这个controller，主要是用来操作对用户信用积分
 **/

@Slf4j
@RequestMapping(value = "/userCredit")
@Controller
public class UserCreditController {


    @Autowired
    UserCreditDao userCreditDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    UserDao userDao;

    @Autowired
    OrdersDao ordersDao;
    @Autowired
    UserCreditService userCreditService;


    /**
     * 进行页面跳转，主要是跳转到购买者给商品发布者的信用积分页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/toAddCredit/{ordersId}")
    public String getCredit(HttpSession session, @PathVariable Integer ordersId, Model model){
        User user = (User)session.getAttribute("currentUser");

        Orders ordersDaoOne = ordersDao.getOne(ordersId);
        Goods daoOne = goodsDao.getOne(ordersDaoOne.getGoodsId());

        model.addAttribute("publishId", daoOne.getPublishMan());
        model.addAttribute("orderId", ordersId);
        model.addAttribute("buyId", user.getId());
        return "goods/addCredit";
    }


    /**
     * 保存一个信用积分
     * @param userCredit
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add_credit")
    public Integer addCredit(UserCredit userCredit){
        try{
            userCredit.setStatus(1);
            userCreditDao.save(userCredit);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 判断是否已经存在一条这样的记录
     * @param orderId
     * @param publishId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isExist")
    public Integer isExist(HttpSession session,@RequestParam Integer orderId, @RequestParam Integer publishId){
        User user = (User)session.getAttribute("currentUser");
        UserCredit userCredit = new UserCredit();
        userCredit.setBuyUser(user.getId());
        userCredit.setOrderId(orderId);
        userCredit.setPublishUser(publishId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, matcher);
        try {
            boolean exists = userCreditDao.exists(example);
            if (exists){
                return 1;
            }else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 500;
    }


    /**
     * 主要是跳转到查看用户信用积分弹出窗口
     * @param publishMan
     * @param model
     * @return
     */
    @RequestMapping(value = "/show_credit/{publishMan}")
    public String showCredit(@PathVariable Integer publishMan, Model model){
        User one = userDao.getOne(publishMan);

        List<Long> dataLists = new ArrayList<Long>();

        // 1: 查看用户是否认证
        String vertifyVal = "";
        Integer statusVal = one.getStatus();
        if (statusVal==1){
            vertifyVal = "未认证";
        }else if (statusVal==2){
            vertifyVal = "完成认证";
        }
        model.addAttribute("vertifyVal", vertifyVal);

        // 2: 查看用户是否有异常举报情况(这是管理员给用户添加的)
        Integer falseReport = one.getFalseReport();
        model.addAttribute("falseReport", falseReport);

        // 3： 查看用户信用积分表
        // 交易是否会迟到
        long latesZero = userCreditService.countLates(one.getId(), 0);
        long latesOne = userCreditService.countLates(one.getId(), 1);
        long latesTwo = userCreditService.countLates(one.getId(), 2);
        // 交易的商品质量是否好
        long qualitysZero = userCreditService.countQualitys(one.getId(), 0);
        long qualitysOne = userCreditService.countQualitys(one.getId(), 1);
        long qualitysTwo = userCreditService.countQualitys(one.getId(), 2);
        // 交易的商品是否符合描述
        long descriptionsZero = userCreditService.countDescriptions(one.getId(), 0);
        long descriptionsOne = userCreditService.countDescriptions(one.getId(), 1);
        long descriptionsTwo = userCreditService.countDescriptions(one.getId(), 2);
        // 卖家提供的服务是否好
        long servicesZero = userCreditService.countServices(one.getId(), 0);
        long servicesOne = userCreditService.countServices(one.getId(), 1);
        long servicesTwo = userCreditService.countServices(one.getId(), 2);

        dataLists.add(latesZero); dataLists.add(latesOne); dataLists.add(latesTwo);
        dataLists.add(qualitysZero); dataLists.add(qualitysOne); dataLists.add(qualitysTwo);
        dataLists.add(descriptionsZero); dataLists.add(descriptionsOne); dataLists.add(descriptionsTwo);
        dataLists.add(servicesZero); dataLists.add(servicesOne); dataLists.add(servicesTwo);

        log.info("---> 数据： "+dataLists);

        model.addAttribute("dataLists", dataLists);
        return "manager/showUserCredit";
    }


    /**
     * 判断买家是否 给卖家进行信用评分了。
     * @param ordersId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ifDiscuss/{ordersId}")
    public Integer ifDiscuss(@PathVariable Integer ordersId,HttpSession session){
        User currentUser = (User)session.getAttribute("currentUser");
        try {
            UserCredit userCredit = userCreditService.searchOne(currentUser.getId(), ordersId);
            if (userCredit!=null){
                return 0;
            }else{
                return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据订单id，查询这一次订单的信用积分
     * @param ordersId
     * @return
     */
    @RequestMapping(value = "/show_info/{ordersId}")
    public String showInfo(@PathVariable Integer ordersId, HttpSession session, Model model){
        User currentUser = (User)session.getAttribute("currentUser");
        UserCredit userCredit = userCreditService.searchOne(currentUser.getId(), ordersId);
        model.addAttribute("userCredit", userCredit);
        return "goods/checkCredit";
    }

    @ResponseBody
    @RequestMapping(value = "/findOne/{id}")
    public Map<String,Object> findOne(@PathVariable Integer id){
        Map<String, Object> map = new HashMap<>();
        try{
            UserCredit userCredit = userCreditDao.getOne(id);
            map.put("code", 0);
            map.put("data", userCredit);
            map.put("msg","查询OK");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "查询失败");
        }
        return map;
    }


    /**
     * 跳转到反驳页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/refute_info/{id}")
    public String refuteInfo(@PathVariable Integer id, Model model){
        UserCredit userCredit = userCreditDao.getOne(id);
        model.addAttribute("userCredit", userCredit);
        return "goods/refuteCredit";
    }


    /**
     * 反驳请求
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refute_credit")
    public Integer refuteCredit(UserCredit userCredit){
        try {
            Integer creditId = userCredit.getId();
            UserCredit one = userCreditDao.getOne(creditId);
            one.setStatus(2);
            one.setCreateTime(new Date());
            one.setPublishAddress(userCredit.getPublishAddress());
            one.setPublishDescription(userCredit.getPublishDescription());
            userCreditDao.saveAndFlush(one);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /***
     * 查询所有 有矛盾的信用评分，
     * @param page
     * @param limit
     * @param firstTime
     * @param lastTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/credit_list")
    public Map<String, Object> creditList(Integer page, Integer limit, String firstTime, String lastTime){
        Map<String, Object> result = new HashMap<>();
        try {
            // 创建查询规格对象
            Specification<UserCredit> specification = (Root<UserCredit> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

                SimpleDateFormat fm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date st1 = new Date();
                Date st2 = new Date();
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(StringUtils.isNotEmpty(firstTime) && StringUtils.isNotEmpty(lastTime)){
                    try {
                        st1=fm.parse(firstTime);
                        st2=fm.parse(lastTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        log.error("时间格式转换出错，错误愿意为："+e);
                    }
                    predicates.add(cb.between(root.get("createTime").as(Date.class), st1, st2));
                }
                predicates.add(cb.equal(root.get("status").as(Integer.class), 2));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createTime");
            Page<UserCredit> userCreditPage = userCreditDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", userCreditPage.getTotalElements());
            result.put("data", userCreditPage.getContent());

        }catch (Exception e){
            e.printStackTrace();
            log.error("出现错误为："+e);
            result.put("code", 500);
            result.put("msg", "服务器内部，查询所有信用矛盾错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 进行页面跳转，跳转到显示个人信用页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/refute_Info/{id}")
    public String showRefute(@PathVariable Integer id, Model model){
        UserCredit credit = userCreditDao.getOne(id);
        
        // 获取名字
        User buyUser = userDao.getOne(credit.getBuyUser());
        User publisherUser = userDao.getOne(credit.getPublishUser());

//        // 获取图片链接
//        String buyAddress = credit.getBuyAddress();
//        String publishAddress = credit.getPublishAddress();
//
//        // 获取描述
//
//        String buyDescription = credit.getBuyDescription();
//        String publishDescription = credit.getPublishDescription();

        model.addAttribute("buyUser", buyUser);
        model.addAttribute("publisherUser", publisherUser);
//        model.addAttribute("buyAddress", buyAddress);
//        model.addAttribute("publishAddress", publishAddress);
//        model.addAttribute("buyDescription", buyDescription);
//        model.addAttribute("publishDescription", publishDescription);
        model.addAttribute("credit", credit);
        return "admin/checkRefute";
    }


}
