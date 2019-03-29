package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.GoodsDao;
import com.gosuncn.shop.dao.ItemsDao;
import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.*;
import com.gosuncn.shop.service.GoodsService;
import com.gosuncn.shop.service.UserService;
import com.gosuncn.shop.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Author: chenxihua
 * @Date: 2019/2/17:12:55
 * @Version 1.0
 **/
@Slf4j
@RequestMapping(value = "/admins")
@Controller
public class adminController {

    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ItemsDao itemsDao;
    @Autowired
    SchoolDao schoolDao;

    /**
     * 查询所有管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/admins_list")
    public Map<String, Object> adminsList(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("adminName");

            // 创建查询规格对象
            Specification<User> specification = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("realName").as(String.class), "%"+keyword+"%"));
                }
                predicates.add(cb.equal(root.get("type").as(Integer.class), 4));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<User> users = userDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", users.getTotalElements());
            result.put("data", users.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 删除一个管理员
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/admins_delete/{userId}")
    public Integer adminsDelete(@PathVariable Integer userId){
        try{
            userDao.deleteById(userId);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 跳转到新增管理员页面
     * @return
     */
    @RequestMapping(value = "/admins_view")
    public String adminsView(){
        return "admin/addAdmins";
    }

    /**
     * 新增一个管理员
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/admins_add")
    public Integer adminsAdd(User user){
        try {
            String adminEmail = user.getEmail();
            String encryptPass = PasswordHelper.encryptPassword("123456", adminEmail);

            user.setPassword(encryptPass);
            user.setSalt(adminEmail);
            user.setType(4);
            user.setStatus(1);
            user.setSignTime(new Date());

            userDao.save(user);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 用于管理员，登录首页时，显示注册总人数，购买总数，等等操作。
     * @return
     */
    @ResponseBody
    @RequestMapping("/sys_view")
    public Map<String, Object> sysView(){
        Map<String, Object> result = new HashMap<>();
        try{
            // 查出系统注册总人数
            long userCounts = userDao.count();
            // 商品发布总数
            long goodCounts = goodsDao.count();
            // 查询所有完成购买的商品
            List<Goods> buyLists = goodsService.finishBuy();
            long buyCounts = buyLists.size();
            // 查询今天之内注册的人数
            List<User> todaySign = userService.todaySign();
            long todayCounts = todaySign.size();
            result.put("code", 0);
            result.put("msg", "查询数据成功!");
            result.put("userCounts", userCounts);
            result.put("goodCounts", goodCounts);
            result.put("buyCounts", buyCounts);
            result.put("todayCounts", todayCounts);
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询数据失败!");
        }
        return result;
    }


    /**
     * 这个方法，是将数据返回给饼状图
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/good_different")
    public Map<String, Object> goodBingPic(){
        Map<String, Object> result = new HashMap<>();
        List<ShowDatas> showDatasList = new ArrayList<>();
        try {
            List<Items> all = itemsDao.findAll();
            for (Items item : all) {
                ShowDatas showDatas = new ShowDatas();

                long numVal = goodsService.goodTypes(item.getId());
                showDatas.setKey(item.getName());
                showDatas.setNumData(numVal);

                showDatasList.add(showDatas);
            }
            result.put("code", 0);
            result.put("data", showDatasList);
            result.put("msg", "所有类型查询成功!");
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询失败！");
        }
        return result;
    }

    /**
     * 找到某一个用户（也可以是管理员用户）
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findOne/{id}")
    public Map<String, Object> findAdminById(@PathVariable Integer id){
        Map<String, Object> result = new HashMap<>();
        try{
            User user = userDao.getOne(id);
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("data", user);
            log.info("---> 数据： "+user);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询成功");
        }
        return result;
    }

    /**
     * 显示商品状态，（在管理员登录页面显示）
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shop_status")
    public Map<String, Object> shopStatus(){
        Map<String, Object> result = new HashMap<>();
        List<ShowDatas> showDatasList = new ArrayList<>();
        try {
            for (int i=0; i<=4; i++){
                ShowDatas showDatas = new ShowDatas();
                long goodStatus = goodsService.goodStatus(i);
                showDatas.setKey("i");
                showDatas.setNumData(goodStatus);
                showDatasList.add(showDatas);
            }
            result.put("code", 0);
            result.put("success", true);
            result.put("data", showDatasList);
            result.put("msg", "所有商品状态查询成功!");
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("success", false);
            result.put("msg", "查询失败！");
        }
        return result;
    }


    /**
     * 用于管理员显示数据，在数据统计页面，显示最高的数量
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/show_max_datas")
    public Map<String, Object> showMaxDatas(){
        Map<String, Object> result = new HashMap<>();
        try {
            // 1： 找到发布二手商品最多的学校
            List<Integer> maxPublishs = goodsService.maxPublicSchool();
            Integer maxSize = maxPublishs.size();
            if (maxSize==1){
                School maxSchool = schoolDao.getOne(maxPublishs.get(0));
                result.put("maxPubSchool", maxSchool.getFullName()+"(单位/件)");
            }else{
                String allName = "";
                for (int i = 0; i <maxSize; i++) {
                    School schoolDaoOne = schoolDao.getOne(maxPublishs.get(i));
                    allName += schoolDaoOne.getFullName()+";";
                }
                result.put("maxPubSchool", allName+"(单位/件)");
            }

            // 2： 找到交易二手商品最多的学校
            List<Integer> maxTrades = goodsService.maxTradeSchool();
            Integer tradeSize = maxTrades.size();
            if (tradeSize==1){
                School tradeSch = schoolDao.getOne(maxTrades.get(0));
                result.put("maxTradeSchool", tradeSch.getFullName()+"(单位/件)");
            }else {
                String allStr = "";
                for (int i = 0; i <tradeSize; i++) {
                    School daoOne = schoolDao.getOne(maxTrades.get(i));
                    allStr += daoOne.getFullName()+";";
                }
                result.put("maxTradeSchool", allStr+"(单位/件)");
            }

            // 3： 找到发布最多的用户
            List<Integer> pubIds = userService.maxPublishs();
            Integer pubLength = pubIds.size();
            if (pubLength==1){
                User user = userDao.getOne(pubIds.get(0));
                result.put("maxPublishUser", user.getRealName()+"(单位/件)");
            }else {
                String userNames = "";
                for (int i = 0; i <pubLength; i++) {
                    User one = userDao.getOne(pubIds.get(i));
                    userNames += one.getRealName()+";";
                }
                result.put("maxPublishUser", userNames+"(单位/件)");
            }

            // 4： 找到交易最多的用户
            List<Integer> tradIds = userService.maxTrades();
            Integer traLength = tradIds.size();
            if (traLength==1){
                User oneUser = userDao.getOne(tradIds.get(0));
                result.put("maxTradeUser", oneUser.getRealName()+"(单位/件)");
            }else {
                String userNameStr = "";
                for (int i = 0; i <traLength; i++) {
                    User twoUser = userDao.getOne(tradIds.get(i));
                    userNameStr += twoUser.getRealName()+";";
                }
                result.put("maxTradeUser", userNameStr+"(单位/件)");
            }

            // 最终result返回的结果是：
            result.put("code", 0);
            result.put("msg", "查询OK");

            return result;

        }catch (Exception e){
            e.printStackTrace();
            log.error("controller层出现严重错误。");
            result.put("code", 500);
            result.put("msg", "查询失败");
        }
        return result;
    }

    /**
     * 显示性别购买占比
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/show_sex_ratio")
    public Map<String, Object> showSexRatio(){
        Map<String, Object> result = new HashMap<>();
        long manNum = 0;  long womanNum = 0;
        try {
            List<User> userList = goodsService.buySexUser();
            for (User user : userList) {
                if (user.getSex()==1){
                    manNum ++;
                }else{
                    womanNum ++;
                }
            }
            result.put("code", 0);
            result.put("manNum", manNum);
            result.put("womanNum", womanNum);
            result.put("msg", "查询成功");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询失败了，服务器出错");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/show_sex_publish")
    public Map<String, Object> showSexPublish(){
        Map<String, Object> result = new HashMap<>();
        long manPub = 0;  long womanPub = 0;
        try{
            List<User> userList = goodsService.publishSexUser();
            for (User user : userList) {
                if (user.getSex()==1){
                    manPub ++;
                }else{
                    womanPub ++;
                }
            }
            result.put("code", 0);
            result.put("manPub", manPub);
            result.put("womanPub", womanPub);
            result.put("msg", "查询成功");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询失败了，服务器出错");
        }
        return result;
    }


    /**
     * 显示全国各个大区的发布占比
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/show_cn_area")
    public Map<String, Object> showCnPublish(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<ShowDatas> showDatasList = goodsService.publishCnSchool();
            log.info("---> : 大家伙来了："+showDatasList);
            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("data", showDatasList);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("data", new ArrayList<>());
            result.put("msg", "查询失败了，服务器出错");
        }
        return result;
    }


    /**
     * 进行页面跳转，跳转到能查看所有后台注册的用户页面
     * @return
     */
    @RequestMapping(value = "/showUserList")
    public String showUserList(){
        return "admin/showUsers";
    }


    /**
     * 用于管理员，查询所有前端注册的用户
     * @param queryParams
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/users_list")
    public Map<String, Object> usersList(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<User> specification = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("realName").as(String.class), "%"+keyword+"%"));
                }
                predicates.add(cb.between(root.get("type").as(Integer.class), 1,3));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<User> users = userDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", users.getTotalElements());
            result.put("data", users.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，查询用户出现错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 管理员查看知道个人信息
     * @return
     */
    @RequestMapping(value = "/adminInfos")
    public String adminInfos(){
        return "admin/showAdmins";
    }


}
