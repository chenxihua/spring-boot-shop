package com.gosuncn.shop;

import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootShopApplicationTests {

    @Autowired
    UserDao userDao;

    @Autowired
    SchoolDao schoolDao;

    @Test
    public void contextLoads() {

        //User user = new User(null, "aaa", null, "aaa@qq.com", );
        List<School> schools = schoolDao.findAll();
        for (School school : schools) {
            System.out.println(school);
        }

    }

}
