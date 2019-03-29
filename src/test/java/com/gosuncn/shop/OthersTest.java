package com.gosuncn.shop;

import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.UserService;
import com.gosuncn.shop.util.PasswordHelper;
import com.gosuncn.shop.util.SendEmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:15:18
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OthersTest {

//    @Autowired
//    JavaMailSenderImpl mailSender;

    @Autowired
    UserService userService;

    @Test
    public void testSysTime(){
        long sysTime = System.currentTimeMillis();
        log.info("---> " + sysTime);
    }


    @Test
    public void testSendEmail(){
        try {
            SendEmailUtil.sendEmailCode("13414853081@163.com", "1112233");
        } catch (MessagingException e) {
            e.printStackTrace();
            log.info("---> 有异常");
        }
    }

    @Test
    public void testAdminPassword(){
//
        String password = PasswordHelper.encryptPassword("123456", "chenxihua.club@qq.com");
        log.info("----> : "+password);
    }

    @Test
    public void testDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date cTime = c.getTime();
        log.info("1: "+zero+";  ---  2: "+cTime);

    }


    @Test
    public void testToday(){
        List<User> users = userService.todaySign();
        log.info("---->>> : "+ users.size());
    }


    @Test
    public void testMap(){
        Map<Long, Integer> map = new HashMap<>();
        map.put(9L, 2);
        map.put(155L, 3);
        map.put(155L, 4);
        map.put(173L, 17);
        map.put(12L, 1);
        log.info("--->: "+map.get(155L));
    }


    @Test
    public void testArrayList(){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 235); map.put(2, 25); map.put(3, 516); map.put(4, 123); map.put(5, 516); map.put(6, 791); map.put(7, 217);
        List<Integer> ids = new ArrayList<>();
        ids.add(12); ids.add(516); ids.add(1); ids.add(164); ids.add(102); ids.add(516); ids.add(49); ids.add(276);
        Integer max = Collections.max(ids);

        log.info("---> "+ids.get(0));
        log.info("---> "+ids.get(1));

    }

    @Test
    public void testMath(){
        Integer add = (int)((Math.random()*9+1)*1000);
        String goodCode = String.valueOf(add)+String.valueOf(System.currentTimeMillis()).substring(0,8);

        log.info("---> : "+ goodCode);
    }


    @Test
    public void testUrl(){
        String fileUrl = "https://springbootshop-1256969743.cos.ap-guangzhou.myqcloud.com/shop/1551856140899.txt";
        String[] strings = fileUrl.split("/");
        for (String string : strings) {
            log.info("--》 ： "+string);
        }
        log.info("最后一位： -----> ："+strings[strings.length-1]);
    }


    @Test
    public void testHaha() throws IOException {
        String str = "haha.txt";
        String perfixStr = "haha";
        String sourceName = "C:\\images\\";
        File file = new File(sourceName);
        if (!file.exists()){
            file.mkdirs();
        }
        String[] list = file.list();
        if (list.length>0){
            for (int i = 0; i <list.length ; i++) {
                if (str.equals(list[i])){
                    log.info("相同了...");
                    File fileCC = null;
                    fileCC = new File(sourceName+str);
                    if (fileCC.isFile()){
                        fileCC.delete();
                        log.info("删除完成，相同了...");
                    }
                }else{
                    log.info("继续。。。");
                }
            }
        }else {
            log.info("---> 够了");
        }
        File newFile = new File(sourceName+str);
        newFile.createNewFile();
//        newFile.mkdirs();

    }


}
