package com.gosuncn.shop.controller;

import com.gosuncn.shop.entities.Comments;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.CommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: chenxihua
 * @Date: 2019-01-16:17:07
 * 用于订单评价表
 */
@Slf4j
@RequestMapping("/comments")
@RestController
public class CommentsController {

    @Autowired
    CommentsService commentsService;

    /**
     * 根据订单，查询这个订单下的所有评论
     * @param ordersId
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/findComments/{ordersId}")
    public Map<String, Object> findAllComments(@PathVariable("ordersId")Integer ordersId, @RequestParam(value = "page",defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit",defaultValue = "10")Integer limit){
        Map<String, Object> map = new HashMap<>();
        Page<Comments> commentsData = commentsService.findComments(ordersId, page, limit);

        map.put("code", 0);
        map.put("data", commentsData);
        map.put("success", true);
        map.put("msg", "查询所有订单评价成功");
        return map;
    }


    /**
     * 删除某一个订单评论记录
     * @return
     */
    @RequestMapping(value = "/deleteCommentById/{commentId}")
    public Integer deleteCommentsById(@PathVariable("commentId")Integer commentId){
        try {
            commentsService.deleteCommentsById(commentId);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 保存一个订单评论
     * @return
     */
    @RequestMapping(value = "/saveComments")
    public Integer savaCommentsData(HttpSession session,Comments comments){
        try {
            User currentUser = (User)session.getAttribute("currentUser");
            comments.setStartTime(new Date());
            comments.setUserId(currentUser.getId());
            comments.setStatus(1);
            commentsService.saveComments(comments);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}
