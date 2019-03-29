package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.NoticesDao;
import com.gosuncn.shop.entities.Notices;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.util.UpHelper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: chenxihua
 * @Date: 2019/2/22:9:26
 * @Version 1.0
 **/

@Slf4j
@RequestMapping(value = "/notices")
@Controller
public class NoticesController {

    @Autowired
    NoticesDao noticesDao;

    @ResponseBody
    @RequestMapping(value = "/notices_list")
    public Map<String, Object> noticesList(HttpSession session, @RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            User user = (User)session.getAttribute("currentUser");
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<Notices> specification = (Root<Notices> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("title").as(String.class), "%"+keyword+"%"));
                }
                predicates.add(cb.equal(root.get("userId").as(Integer.class), user.getId()));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createTime");
            Page<Notices> notices = noticesDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", notices.getTotalElements());
            result.put("data", notices.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部出现错误了");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }

    // 新增公告页面
    @RequestMapping(value = "/new_notices")
    public String newNotices(){
        return "admin/addNotices";
    }

    /**
     * 这是更新公告页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/updata_notices/{id}")
    public String updataNotices(@PathVariable Integer id, Model model){
        Notices notices = noticesDao.getOne(id);
        model.addAttribute("notices", notices);
        return "admin/updataNotices";
    }

    @RequestMapping(value = "/look_notices/{id}")
    public String lookNotices(@PathVariable Integer id, Model model){
        Notices notices = noticesDao.getOne(id);
        model.addAttribute("notices", notices);
        return "admin/lookNotices";
    }

    // 新增公告事件
    @ResponseBody
    @RequestMapping(value = "/notices_add")
    public Integer noticesAdd(HttpSession session, Notices notices){
        try {
            User user = (User)session.getAttribute("currentUser");
            notices.setUserId(user.getId());
            notices.setCreateTime(new Date());
            notices.setStatus(1);
            noticesDao.save(notices);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 批量删除自己发布的公告
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batch_delete")
    public Integer batchDelete(@RequestParam String ids){
        try{
            // 转成字符串数组
            String[] noticesIds = ids.split("-");
            for (String nid : noticesIds) {
                Integer id = Integer.parseInt(nid);
                noticesDao.deleteById(id);
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 用于显示所有公告
     * @param queryParams
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/notices_view")
    public Map<String, Object> noticesView(@RequestParam Map<String, Object> queryParams){
        Map<String, Object> result = new HashMap<>();
        try{
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<Notices> specification = (Root<Notices> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (keyword != null && !"".equals(keyword)){
                    predicates.add(cb.like(root.get("title").as(String.class), "%"+keyword+"%"));
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createTime");
            Page<Notices> notices = noticesDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", notices.getTotalElements());
            result.put("data", notices.getContent());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部出现错误了,不能查询所有数据");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }
        return result;
    }

    // 下载附件
    @ResponseBody
    @RequestMapping(value = "/download_file/{id}")
    public Integer downloadFile(@PathVariable Integer id){
        try {
            Notices one = noticesDao.getOne(id);
            String fileUrl = one.getFileUrl();
            if (StringUtils.isNotEmpty(fileUrl) && StringUtils.isNotBlank(fileUrl)){
                UpHelper.downloadUrl(fileUrl);
                return 200;
            }else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


}
