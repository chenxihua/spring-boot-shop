package com.gosuncn.shop;

import com.gosuncn.shop.dao.UserCreditDao;
import com.gosuncn.shop.entities.*;
import com.gosuncn.shop.service.*;
import com.gosuncn.shop.service.impl.OrderServiceImpl;
import com.gosuncn.shop.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:9:31
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    ItemsService itemsService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    CollectionService collectionService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    OrderService orderService;

    @Autowired
    UserCreditDao userCreditDao;
    @Autowired
    UserCreditService userCreditService;

    @Test
    public void testItems(){
        List<Items> itemsByAll = itemsService.getItemsByAll();
        for (Items items : itemsByAll) {
            log.info("---> " + items);
        }
    }

    @Test
    public void testSavaGoods(){
        Goods goods = new Goods();
        goods.setName("aaa");
        goods.setAvailable(99);
        goods.setOriginalPrice(199.00);
        goods.setPrice(99.00);
        goods.setPhone("13414853081");
        goods.setStatus(1);

        boolean goodInfo = goodsService.saveGoodInfo(goods);
        log.info("---> " + goodInfo);
    }


    @Test
    public void testFindGood(){
        Goods goodInfo = goodsService.getGoodsInfoById(10);
        log.info("---> : " + goodInfo);
    }


    @Test
    public void testCollectionByIds(){
        List<Integer> goodsByIds = new ArrayList<Integer>();
        goodsByIds.add(2); goodsByIds.add(3); goodsByIds.add(4); goodsByIds.add(5);
        List<Goods> goodsInfos = goodsService.getGoodsInfo(goodsByIds);
        for (Goods goodsInfo : goodsInfos) {
            log.info("---> " + goodsInfo);
        }
    }

    @Test
    public void testSchoolById(){
        School school = schoolService.getSchoolById(6);
        log.info("---> :  " + school);
    }

    @Test
    public void testSaveRecord(){
        Collection collection = new Collection();
        collection.setGoodsId(2);
        collection.setUserId(6);
        collection.setCollectTime(new Date());
        boolean one = collectionService.saveCollectForOne(collection);
        log.info("---> : " + one);
    }

    @Test
    public void testSaveOrder(){
        Orders orders = new Orders();
        orders.setUserId(2); orders.setDetails("nihaoaaaa"); orders.setGoodsId(3);
        boolean b = orderService.saveOrder(orders);
        log.info("----> : " + b);
    }

    @Test
    public void testCollectPage(){
        Page<Collection> collections = collectionService.getCollections(6, 2, 10);
        int totalPages = collections.getTotalPages();
        long totalElements = collections.getTotalElements();
        List<Collection> content = collections.getContent();
        int number = collections.getNumber();
        int size = collections.getSize();
        log.info("---> : totalPages : " + totalPages);
        log.info("---> : totalElements : " + totalElements);
        log.info("---> : number : " + number);
        log.info("---> : size : " + size);
        for (Collection collection : content) {
            log.info("---> : collection : " + collection);
        }
    }


    @Test
    public void testPass(){
        String password = PasswordHelper.encryptPassword("123", "chenxihua@qq.com");
        log.info("password : ---> "+ password);
    }


    @Test
    public void testFindOrders(){
        Orders orderById = orderService.findOrderById(14);
        boolean flags = orderService.updateOrdersStatus(orderById, 0);
        log.info("---> : " + flags);
    }

    @Test
    public void testAdmin(){
        String admin = PasswordHelper.encryptPassword("admin", "admin@qq.com");
        log.info("---> : "+admin);
    }


    @Test
    public void testCredit(){
        UserCredit userCredit = new UserCredit();
        userCredit.setPublishUser(4);
        userCredit.setIsLate(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<UserCredit> example = Example.of(userCredit, matcher);

        try {
            long count = userCreditDao.count(example);
            log.info("---> 总数: "+count);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void testServiceCredit(){
        UserCredit userCredit = userCreditService.searchOne(3, 4);
        log.info("---> : "+userCredit);
    }


}
