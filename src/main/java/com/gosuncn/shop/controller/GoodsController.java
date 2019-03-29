package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.GoodsDao;
import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Goods;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:11:48
 */
@Slf4j
@RequestMapping(value = "/goods")
@Controller
public class GoodsController {


    @Autowired
    GoodsService goodsService;
    @Autowired
    UserDao userDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    SchoolDao schoolDao;


    /**
     * 根据学校的id，与及商品种类id，查询二手商品（因为商品字段里面有发布所在学校）
     * @return
     */
//    @ResponseBody
//    @PostMapping(value = "/searchBuyList")
//    public Map<String, Object> searchGoodsBySchId(@RequestParam("schoolId")Integer schoolId, @RequestParam("itVal")String itVal,
//                                                  @RequestParam(value = "pageIndex",defaultValue = "1")Integer page, @RequestParam(value = "pageLimit",defaultValue = "12")Integer limit,
//                                                  ){
//        Map<String, Object> map = new HashMap<>();
//        try {
//            Page<Goods> goodsPage = goodsService.getGoodsBySchoolId(schoolId, itVal, page, limit);
//
//            map.put("code", 0);
//            map.put("success", true);
//            map.put("data", goodsPage.getContent());
//            map.put("totalCounts", goodsPage.getTotalElements());
//            map.put("number", goodsPage.getNumber());
//            map.put("size", goodsPage.getSize());
//            map.put("msg", "根据学校查询商品成功");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return map;
//    }

    @ResponseBody
    @RequestMapping(value = "/searchBuyList")
    public Map<String, Object> searchBuyList(Integer schoolId, String itVal, @RequestParam(value = "pageIndex",defaultValue = "1")Integer page,
                                             @RequestParam(value = "pageLimit",defaultValue = "12")Integer limit, String keyword){
        Map<String, Object> result = new HashMap<>();
        try {
            // 创建查询规格对象
            Specification<Goods> specification = (Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("name").as(String.class), "%"+keyword+"%"));
                }
                if (schoolId!=null){
                    predicates.add(cb.equal(root.get("schoolId").as(Integer.class), schoolId));
                }
                if (!StringUtils.isBlank(itVal)){
                    predicates.add(cb.equal(root.get("typeId").as(Integer.class), itVal));
                }
                predicates.add(cb.equal(root.get("status").as(Integer.class), 2));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "publishTime");
            Page<Goods> goods = goodsDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("data", goods.getContent());
            result.put("totalCounts", goods.getTotalElements());
            result.put("number", goods.getNumber());
            result.put("size", goods.getSize());
            result.put("msg", "根据学校查询商品成功");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("data", 0);
            result.put("msg", "查询失败");
        }
        return result;
    }


    /**
     * 保存学生用户上传的商品信息
     * @param goods
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveGoodInfos")
    public Integer savePubGoodsInfo(Goods goods, HttpSession session){
        try {
            User user = (User)session.getAttribute("currentUser");
            Integer add = (int)((Math.random()*9+1)*1000);
            String goodCode = String.valueOf(add)+String.valueOf(System.currentTimeMillis()).substring(0,8);

            goods.setGoodCode(goodCode);
            goods.setStatus(1);
            goods.setPublishMan(user.getId());
            goods.setPublishTime(new Date());
            goodsService.saveGoodInfo(goods);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据商品id，进行查询某一商品(这个方法，被商品展示事，点击进去的页面所使用了)
     * @param goodsId
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/findById/{goodsId}")
    public Map<String, Object> findGoodsById(@PathVariable("goodsId")Integer goodsId){
        Map<String, Object> map = new HashMap<>();
        try{
            Goods goods = goodsService.getGoodsInfoById(goodsId);
            // 然后根据这个商品，查询出这个发布人
            User user = userDao.getOne(goods.getPublishMan());
            // 然后根据这个商品，查询出发布所在学校
            School school = schoolDao.getOne(goods.getSchoolId());

            map.put("code", 0);
            map.put("data", goods);
            map.put("dataUser", user);
            map.put("dataSchool", school);
            map.put("msg", "查询单个商品信息成功");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", 500);
            map.put("data", 0);
            map.put("msg", "查询单个商品信息失败");
        }
        return map;
    }

    /**
     * 删除某一个商品，取消发布以及出售了。
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteById/{id}")
    public Integer deleteGoods(@PathVariable("id")Integer id){
        try{
            goodsService.deleteGoods(id);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batch_delete")
    public Integer batchDelete(@RequestParam String ids){
        try{
            // 转成字符串数组
            String[] goodIds = ids.split("-");
            for (String goodId : goodIds) {
                // 然后批量删除
                goodsDao.deleteById(Integer.parseInt(goodId));
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 用于商品审核不通过的时候，可以重新上传商品，让管理员重新审核。
     * @return
     */
    @ResponseBody
    @PutMapping(value = "/updateGoods/{goodsId}")
    public Integer updateGoodsStatus(@PathVariable("goodsId")Integer goodsId){
        try {
            Goods goods = goodsService.getGoodsInfoById(goodsId);
            goods.setStatus(1);
            goodsService.updateGoodsForStatus(goods);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    //  ------------------------     分割线      ---------------------------


    @ResponseBody
    @RequestMapping(value = "/goods_list")
    public Map<String, Object> adminsList(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("goodsName");

            // 创建查询规格对象
            Specification<Goods> specification = (Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("name").as(String.class), "%"+keyword+"%"));
                }
                predicates.add(cb.equal(root.get("status").as(Integer.class), 1));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            Page<Goods> goods = goodsDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", goods.getTotalElements());
            result.put("data", goods.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，查询审核商品信息错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/publish_list")
    public Map<String, Object> publishList(HttpSession session, @RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        User user = (User)session.getAttribute("currentUser");
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("goodsName");

            // 创建查询规格对象
            Specification<Goods> specification = (Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("name").as(String.class), "%"+keyword+"%"));
                }
                predicates.add(cb.equal(root.get("publishMan").as(Integer.class), user.getId()));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "publishTime");
            Page<Goods> goods = goodsDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", goods.getTotalElements());
            result.put("data", goods.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，查询自己发布的商品信息错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }

    /**
     * 商品是否通过审核, 这个为通过
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/passCheck/{goodsId}")
    public Integer passChecked(@PathVariable Integer goodsId){
        try{
            Goods goodsInfoById = goodsService.getGoodsInfoById(goodsId);
            goodsInfoById.setStatus(2);
            goodsService.updateGoodsForStatus(goodsInfoById);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 商品是否通过审核， 这个为不通过
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/defineCheck/{goodsId}")
    public Integer falseChecked(@PathVariable Integer goodsId){
        try{
            Goods goodsInfoById = goodsService.getGoodsInfoById(goodsId);
            goodsInfoById.setStatus(0);
            goodsService.updateGoodsForStatus(goodsInfoById);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    //       --------------              分割线（2019/3/5）          --------------


    @RequestMapping(value = "/show_history/{publishMan}")
    public String showList(@PathVariable Integer publishMan, Model model){
        model.addAttribute("publishMan", publishMan);
        return "goods/historyList";
    }

    /**
     * 查看个人历史发布商品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/history_list/{publishMan}")
    public Map<String, Object> historyList(@PathVariable Integer publishMan, @RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try {
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());

            // 创建查询规格对象
            Specification<Goods> specification = (Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(cb.equal(root.get("publishMan").as(Integer.class), publishMan));
                predicates.add(cb.between(root.get("status").as(Integer.class), 2,4));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "publishTime");
            Page<Goods> goods = goodsDao.findAll(specification, pageable);
            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", goods.getTotalElements());
            result.put("data", goods.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部，查询历史商品信息错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    @RequestMapping(value = "/oneInfos/{id}")
    public String showOneInfos(@PathVariable Integer id, Model model){
        Goods daoOne = goodsDao.getOne(id);
        School school = schoolDao.getOne(daoOne.getSchoolId());
        model.addAttribute("school", school);
        model.addAttribute("goodInfo", daoOne);
        return "goods/oneInfo";
    }

}
