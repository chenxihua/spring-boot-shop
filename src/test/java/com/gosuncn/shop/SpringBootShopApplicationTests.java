package com.gosuncn.shop;

import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.ProvinceService;
import com.gosuncn.shop.service.SchoolService;
import com.gosuncn.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.UNKNOWN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootShopApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    UserDao userDao;

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
//        log.info(UUID.randomUUID().toString().substring(0, 6));
        int[] arrs = {123,431,399,15,50,391,31,98,119,2};
        Arrays.sort(arrs);

        int[] ints = Arrays.copyOf(arrs, 4);
        int lens = arrs.length;
        for (int i = 0; i < ints.length; i++) {
            log.info("-->: " + ints[i]);
        }
    }

//    public User findOne(User user){
//
//    } user0_.real_name=? and (user0_.account like ?) and (user0_.email like ?)

    @Test
    public void testSearch(){
        User user = new User();
        user.setAccount("aaa");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
//                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains())
//                .withMatcher("account", ExampleMatcher.GenericPropertyMatchers.startsWith())
//                .withIgnorePaths("realName");

//                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
//                .withIgnorePaths("focus");
//                .withMatcher("account", ExampleMatcher.GenericPropertyMatchers.startsWith());
        Example<User> example = Example.of(user, matcher);
//        User user1 = userDao.findOne(example).orElse(null);
//        log.info("---->user: " + user1);
//        Optional<User> one = userDao.findOne(example);

        boolean exists = userDao.exists(example);
        log.info("--->" + exists);

    }

    @Test
    public void testUtils(){
        log.info("---> : " + userService.findByConditionsToUser("chenxihua@qq.com"));
    }



    @Test
    public void contextLoads() {

        String url = "hsoanaelaef";
        String ahah = StringUtils.reverse(url);
        log.info("--->" + ahah);

        //User user = new User(null, "aaa", null, "aaa@qq.com", );
//        List<School> schools = schoolDao.findAll();
//        for (School school : schools) {
//            System.out.println(school);
//        }
    }

}
