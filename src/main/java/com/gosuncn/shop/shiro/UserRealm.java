package com.gosuncn.shop.shiro;

import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.UserService;
import com.gosuncn.shop.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: chenxihua
 * @Date: 2018-11-22:14:35
 * 仅限于学生用户登录验证使用。
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("-----> 来到了UserRealm");
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //2. 从 UsernamePasswordToken 中来获取 username
        String username = upToken.getPrincipal().toString();

        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        User user = userService.findByConditionsToUser(username);
        log.info("数据库中的：password: ---> " + user.getPassword());

        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if (user==null){
            // 未知账号异常
            throw new UnknownAccountException();
        }
        // 5. 验证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()), getName());

        return authenticationInfo;
    }
}
