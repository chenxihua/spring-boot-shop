package com.gosuncn.shop.service;

import com.gosuncn.shop.dao.CommentsDao;
import com.gosuncn.shop.entities.Comments;
import org.springframework.data.domain.Page;

/**
 * @author: chenxihua
 * @Date: 2019-01-16:15:53
 * 这个是用于订单评价的service层、
 */
public interface CommentsService {




    /**
     * 根据订单编号，查询这个订单的所有评论。
     * @return
     */
    public Page<Comments> findComments(Integer ordersId, Integer page, Integer limit);


    /**
     * 保存一个评论信息
     * @return
     */
    public boolean saveComments(Comments comments);


    /**
     * 删除一条评论
     * @param id
     */
    public void deleteCommentsById(Integer id);
}
