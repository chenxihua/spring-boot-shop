package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.CommentsDao;
import com.gosuncn.shop.entities.Comments;
import com.gosuncn.shop.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @author: chenxihua
 * @Date: 2019-01-16:15:56
 * 这个类，是用于评论表的接口
 */
@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    CommentsDao commentsDao;

    /**
     * 根据订单id，查询这个订单下的所有评论
     * @param ordersId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<Comments> findComments(Integer ordersId, Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Comments comments = new Comments();
        comments.setOrdersId(ordersId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Comments> example = Example.of(comments, matcher);

        Page<Comments> commentsPage = commentsDao.findAll(example, pageable);

        return commentsPage;
    }

    /**
     * 保存一条评论
     * @param comments
     * @return
     */
    @Override
    public boolean saveComments(Comments comments) {
        Comments saveComments = commentsDao.save(comments);
        if (saveComments!=null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 删除一条评论
     * @param id
     */
    @Override
    public void deleteCommentsById(Integer id) {
        commentsDao.deleteById(id);
    }
}
