package com.gosuncn.shop.util;

import com.gosuncn.shop.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

/**
 * @author: chenxihua
 * @Date: 2018-11-21:9:34
 *
 * 用于用户注册时，用MD5算法加密密码
 */
@Slf4j
public class PasswordHelper {

    private static String algorithmName = "MD5";
    private static Integer hashIterations = 1024;

    /**
     * 这个方法是设置MD5加密后，得到新密码的方法
     * @param password
     */
    public static User encryptPassword(String password){
        User user = new User();
        String saltString = UUID.randomUUID().toString().substring(0, 8);
        if (!StringUtils.isBlank(password) && !StringUtils.isEmpty(password)){
            String newPassword = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(saltString),
                    hashIterations).toHex();
            user.setPassword(newPassword);
            user.setSalt(saltString);
            return user;
        }else{
            return null;
        }
    }

}
