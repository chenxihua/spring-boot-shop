package com.gosuncn.shop.service;

import com.gosuncn.shop.dao.CollectionDao;
import com.gosuncn.shop.entities.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2019-01-08:8:48
 * 这是用于收藏表的服务层
 */
public interface CollectionService {


    /**
     * 获取个人所有的收藏列表
     * @return
     */
    public Page<Collection> getCollections(Integer uid, Integer page, Integer limit);


    /**
     * 保存一条收藏记录
     * @return
     */
    public boolean saveCollectForOne(Collection collection);

    /**
     * 删除一条收藏记录
     * @param collectId
     */
    public void deleteCollectById(Integer collectId);

}
