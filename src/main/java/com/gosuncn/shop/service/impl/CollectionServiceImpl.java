package com.gosuncn.shop.service.impl;

import com.gosuncn.shop.dao.CollectionDao;
import com.gosuncn.shop.entities.Collection;
import com.gosuncn.shop.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2019-01-08:8:58
 */
@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    CollectionDao collectionDao;


    /**
     * 根据用户id，查询用户的所有收藏列表
     * @param uid
     * @return
     */
    @Override
    public Page<Collection> getCollections(Integer uid, Integer page, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "collectTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Collection collection = new Collection();
        collection.setUserId(uid);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Collection> example = Example.of(collection, matcher);
        Page<Collection> pageCollection = collectionDao.findAll(example, pageable);

        return pageCollection;
    }

    /**
     * 保存一条实体收藏记录
     * @param collection
     * @return
     */
    @Override
    public boolean saveCollectForOne(Collection collection) {
        Collection saveRecord = collectionDao.save(collection);
        if (saveRecord!=null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 删除一条收藏记录
     * @param collectId
     */
    @Override
    public void deleteCollectById(Integer collectId) {
        collectionDao.deleteById(collectId);
    }
}
