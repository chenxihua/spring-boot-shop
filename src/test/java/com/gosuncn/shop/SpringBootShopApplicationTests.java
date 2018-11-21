package com.gosuncn.shop;

import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.ProvinceService;
import com.gosuncn.shop.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootShopApplicationTests {

    @Autowired
    UserDao userDao;
    @Autowired
    SchoolService schoolService;

    @Autowired
    ProvinceService provinceService;

    @Test
    public void testProvince(){
        List<Province> provinces = provinceService.getAllProvince();
        for (Province province : provinces) {
            log.info("------->" + province);
        }
    }

    @Test
    public void testSaveSchool(){
        School school = new School();
        school.setAreaId(7);
        school.setFullName("韩山师范1");
        school.setName("韩师1");
        school.setAddress("汕头1");
        boolean schoolInfo = schoolService.saveSchoolInfo(school);
        log.info("结果是：---" + schoolInfo);
    }

    @Test
    public void testPassword(){
        String algorithmName = "MD5";
        String password = "123";
        Integer hashIterations = 1024;
        String newPassword = new SimpleHash(
                algorithmName,  //加密算法
                password,  //密码
                ByteSource.Util.bytes("username"),  //盐值 username+salt
                hashIterations  //hash次数
        ).toHex();
        log.info("测试加密密码----->" + newPassword);
    }

    @Test
    public void testUUID(){
        log.info(UUID.randomUUID().toString().substring(0, 6));
    }


    @Test
    public void contextLoads() {
        //User user = new User(null, "aaa", null, "aaa@qq.com", );
//        List<School> schools = schoolDao.findAll();
//        for (School school : schools) {
//            System.out.println(school);
//        }
    }

}
