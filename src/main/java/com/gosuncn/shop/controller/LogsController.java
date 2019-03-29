package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.SysLogDao;
import com.gosuncn.shop.entities.Collection;
import com.gosuncn.shop.entities.SysLog;
import com.gosuncn.shop.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: chenxihua
 * @Date: 2019/2/26:9:30
 * @Version 1.0
 **/
@Slf4j
@RequestMapping(value = "/logs")
@Controller
public class LogsController {

    @Autowired
    SysLogDao sysLogDao;


    /**
     * 按条件查询日志
     * @param page
     * @param limit
     * @param firstTime
     * @param lastTime
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sysLogs_list")
    public Map<String, Object> sysLogsList(Integer page, Integer limit, String firstTime, String lastTime, String keyword, HttpServletRequest httpServletRequest){

        User currentUser = (User)httpServletRequest.getSession().getAttribute("currentUser");
        long creationTime = httpServletRequest.getSession().getCreationTime();

        log.info("user ---> : " +currentUser);
        log.info("time ---> : " +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creationTime));

        Map<String, Object> result = new HashMap<>();
        try {
            // 创建查询规格对象
            Specification<SysLog> specification = (Root<SysLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

                SimpleDateFormat fm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d1 = new Date();
                Date d2 = new Date();
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(StringUtils.isNotEmpty(firstTime) && StringUtils.isNotEmpty(lastTime)){
                    try {
                        d1=fm.parse(firstTime);
                        d2=fm.parse(lastTime);
                    } catch (ParseException ee) {
                        ee.printStackTrace();
                        log.error("时间格式转换出错："+ee);
                    }
                    predicates.add(cb.between(root.get("createDate").as(Date.class), d1, d2));
                }
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("username").as(String.class), "%"+keyword+"%"));
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createDate");
            Page<SysLog> sysLogs = sysLogDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", sysLogs.getTotalElements());
            result.put("data", sysLogs.getContent());

        }catch (Exception e){
            e.printStackTrace();
            log.error("出现错误为："+e);
            result.put("code", 500);
            result.put("msg", "服务器内部，查询所有日志记录错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }


    /**
     * 删除单个日志
     * @param paraUrl
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete_logs/{paraUrl}")
    public Integer deleteLogs(@PathVariable long paraUrl){
        try {
            sysLogDao.deleteById(paraUrl);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 批量删除日志
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batch_delete")
    public Integer batchDelete(@RequestParam String ids){
        try{
            // 转成字符串数组
            String[] logsId = ids.split("-");
            for (String logId : logsId) {
                sysLogDao.deleteById(Long.parseLong(logId));
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}
