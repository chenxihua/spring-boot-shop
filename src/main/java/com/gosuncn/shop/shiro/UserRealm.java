package com.gosuncn.shop.shiro;

import com.gosuncn.shop.entities.*;
import com.gosuncn.shop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-11-22:14:35
 * 仅限于学生用户登录验证使用。
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    @Autowired
    UserRolesService userRolesService;
    @Autowired
    RolesService rolesService;
    @Autowired
    RolesPermissionService rolesPermissionService;
    @Autowired
    PermissionService permissionService;


    /**
     * 添加角色和授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        String username = (String)principalCollection.getPrimaryPrincipal();
        log.info("---> 授权 username: "+username);
        User user = userService.findByConditionsToUser(username);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<UserRoles> userRoles = userRolesService.getUserRoles(user.getId());
        for (UserRoles userRole : userRoles) {
            Roles roles = rolesService.getRolesById(userRole.getRolesId());
            // 添加角色
            simpleAuthorizationInfo.addRole(roles.getName());

            // 这个角色下，有多少个权限
            List<RolesPermission> rolesPermissions = rolesPermissionService.getRolesPermissions(roles.getId());
            for (RolesPermission rolesPermission : rolesPermissions) {
                Permission permission = permissionService.getPermissionById(rolesPermission.getPermissionId());
                // 添加权限
                simpleAuthorizationInfo.addStringPermission(permission.getName());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 登录验证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
//        UsersToken usersToken = (UsersToken)token;
        log.info("---> userToken Realm: "+upToken);
        //2. 从 UsernamePasswordToken 中来获取 username
        String username = upToken.getPrincipal().toString();

        try {
            //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
            User user = userService.findByConditionsToUser(username);
            //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
            if (user!=null){
                // 5. 验证
                SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(),
                        ByteSource.Util.bytes(user.getSalt()), getName());

                return authenticationInfo;
            }
        }catch (AuthenticationException e){
            e.printStackTrace();
            log.error("出现异常为："+e);
        }
        return null;
    }
}
