package com.gosuncn.shop;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: chenxihua
 * @Date: 2018-11-30:14:45
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    /**
     * 用于测试redis实例
     */

//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @Test
//    public void testRedisOne(){
//        redisTemplate.opsForValue().set("gasun","zhong");
//        log.info("--->" + redisTemplate.toString());
//    }
//
//    @Test
//    public void testDele(){
//        redisTemplate.delete("test:set");
//        log.info("--->: 删除完毕.");
//    }
//
//    @Test
//    public void testDateFor(){
//        // 1545633665923
//        long currentDate = System.currentTimeMillis();
//        log.info("----> " + currentDate);
//    }

}
