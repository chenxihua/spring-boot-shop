package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.CollectionDao;
import com.gosuncn.shop.entities.Collection;
import com.gosuncn.shop.entities.Goods;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.CollectionService;
import com.gosuncn.shop.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
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
 * @Date: 2019-01-08:8:43
 * 用于收藏的controller
 */
@Slf4j
@RequestMapping(value = "/collect")
@Controller
public class CollectionController {

    @Autowired
    CollectionService collectionService;
    @Autowired
    GoodsService goodsService;

    @Autowired
    CollectionDao collectionDao;

    /**
     * 查询某一个用户的所有收藏商品记录
     * @param session
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/collect_list")
    public Map<String, Object> collectList(HttpSession session, Integer page, Integer limit, String firstTime){
        Map<String, Object> result = new HashMap<>();
        try {
            User currentUser = (User)session.getAttribute("currentUser");
            // 创建查询规格对象
            Specification<Collection> specification = (Root<Collection> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

                SimpleDateFormat fm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = new Date();
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(StringUtils.isNotEmpty(firstTime)){
                    try {
                        date1=fm.parse(firstTime);
                    } catch (ParseException ee) {
                        ee.printStackTrace();
                        log.error("时间格式转换出错："+ee);
                    }
                    predicates.add(cb.between(root.get("collectTime").as(Date.class), date1, new Date()));
                }
                predicates.add(cb.equal(root.get("userId").as(Integer.class), currentUser.getId()));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "collectTime");
            Page<Collection> collections = collectionDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", collections.getTotalElements());
            result.put("data", collections.getContent());

        }catch (Exception e){
            e.printStackTrace();
            log.error("出现错误为："+e);
            result.put("code", 500);
            result.put("msg", "服务器内部，查询个人收藏记录错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 在浏览商品时，可以对一个商品进行收藏
     * @param session
     * @param goodIdVal
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveCollRecord/{goodsId}")
    public Integer saveCollectionRecord(HttpSession session, @PathVariable("goodsId")Integer goodIdVal){
        Map<String, Object> map = new HashMap<>();
        User user = (User)session.getAttribute("currentUser");
        try {
            Collection collection = new Collection();
            collection.setGoodsId(goodIdVal);
            collection.setUserId(user.getId());
            collection.setCollectTime(new Date());
            collection.setStatus(1);
            collectionService.saveCollectForOne(collection);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 删除一条收藏记录
     * @param paraUrl
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteById/{paraUrl}")
    public Integer deleteCollectById(@PathVariable("paraUrl")Integer paraUrl){
        try {
            collectionService.deleteCollectById(paraUrl);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 批量删除收藏的二手商品列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batch_delete")
    public Integer batch_delete(@RequestParam String ids){
        try {
            // 转成字符串数组
            String[] collectIds = ids.split("-");
            for (String collectId : collectIds) {
                collectionDao.deleteById(Integer.parseInt(collectId));
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            log.error("批量删除收藏列表出现异常，异常为："+e);
        }
        return -1;
    }

}
