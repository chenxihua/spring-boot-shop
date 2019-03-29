package com.gosuncn.shop.controller;

import com.gosuncn.shop.annonation.MyLog;
import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.dao.UserRolesDao;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.entities.UserRoles;
import com.gosuncn.shop.service.UserRolesService;
import com.gosuncn.shop.service.UserService;
import com.gosuncn.shop.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:10:24
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    UserRolesService userRolesService;
    @Autowired
    SchoolDao schoolDao;
    @Autowired
    UserRolesDao userRolesDao;
    @Autowired
    UserDao userDao;

    /**
     * 对输入昵称框进行判断。判断这个昵称是否已经被判断
     * @param accountValue
     * @param pattern
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/accountVerify")
    public Map<String, Object> searchByConditionsForAccount(@RequestParam("accountValue") String accountValue,
                                                            @RequestParam("pattern") String pattern){
        Map<String, Object> map = new HashMap<>();
        User user = new User();
        user.setAccount(accountValue);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<User> example = Example.of(user, matcher);
        boolean conditions = userService.findByConditions(example);
        if (conditions){
            map.put("code", 0);
            map.put("success", true);
            map.put("msg", "昵称已存在，请重新输入昵称");
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "可以进行注册昵称");
        }
        return map;
    }


    /**
     * 对邮箱输入框进行判断，判断这个邮箱是否已经存在
     * @param emailValue
     * @param pattern
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/emailVerify")
    public Map<String, Object> searchByConditionsForEmail(@RequestParam("emailValue")String emailValue,
                                                          @RequestParam("pattern")String pattern){
        Map<String, Object> map = new HashMap<>();
        User user = new User();
        user.setEmail(emailValue);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();
        Example<User> example = Example.of(user, matcher);
        boolean conditions = userService.findByConditions(example);
        if (conditions){
            map.put("code", 0);
            map.put("success", true);
            map.put("msg", "邮箱已经注册，请重新输入邮箱");
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "可以进行注册邮箱");
        }
        return map;

    }

    /**
     * 注册系统新用户（学生注册）
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveByRegister")
    public Integer saveUser(User user){
        String userEmail = user.getEmail();
        try {
            String encryptPassword = PasswordHelper.encryptPassword(user.getPassword(), userEmail);
            if (StringUtils.isEmpty(encryptPassword) && StringUtils.isBlank(encryptPassword)){
                return 101;  // 表示是空的值
            }
            user.setPassword(encryptPassword);
            user.setSalt(userEmail);
            user.setFalseReport(0);
            user.setStatus(1);
            user.setSignTime(new Date());
            User save = userDao.save(user);
            if (save!=null){
                // 立刻分配权限，分配前端用户的权限
                UserRoles userRoles = new UserRoles();
                userRoles.setUserId(save.getId());
                userRoles.setRolesId(5);
                userRoles.setStatus(1);
                userRolesDao.save(userRoles);
                return 0;
            }else {
                return 500;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 学生用户登录系统
     * @param emailValue
     * @param passValue
     * @return
     */
    @MyLog(value = "登录系统")
    @ResponseBody
    @PostMapping("/loginSyst")
    public Map<String, Object> loginSyst(@RequestParam("emailValue") String emailValue,
                                         @RequestParam("passValue")String passValue){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(emailValue) && StringUtils.isBlank(passValue)){
            map.put("code", 0); map.put("success", false); map.put("msg", "用户名或密码为空! ");
            return map;
        }
        Subject currentUser = SecurityUtils.getSubject();
        //验证是否登录成功
        if (!currentUser.isAuthenticated()){
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(emailValue, passValue);
            log.info("===> usersToken1: "+token);
            User tUser = null;
            try {
                // 执行登录.
                currentUser.login(token);
                //把当前用户放入session
                Session session = currentUser.getSession();
                tUser = userService.findByConditionsToUser(emailValue);
                session.setAttribute("currentUser",tUser);
            }catch(UnknownAccountException uae){
                map.put("code", 1);
                map.put("success", false);
                map.put("msg", "对用户【" + emailValue + "】进行登录验证...验证未通过。原因：未知账号");
                return map;
            }catch(IncorrectCredentialsException ice){
                map.put("code", 1);
                map.put("success", false);
                map.put("msg", "对用户【" + emailValue + "】进行登录验证...验证未通过。原因：密码不正确");
                return map;
            }catch (DisabledAccountException sae){
                map.put("code", 1);
                map.put("success", false);
                map.put("msg", "对用户【" + emailValue + "】进行登录验证...验证未通过。原因：帐号已经禁止登录");
                return map;
            }catch(AuthenticationException ae){
                map.put("code", 1);
                map.put("success", false);
                map.put("msg", "对用户【" + emailValue + "】进行登录验证...验证未通过。原因：输入账户异常");
                return map;
            }
            map.put("code", 0);
            map.put("success", true);
            map.put("data", tUser);
            map.put("msg", "用户验证成功");
            return map;
        }else {
            map.put("code", 1);
            map.put("success", false);
            map.put("msg", "用户已经过认证，不需要重新再认证");
            return map;
        }
    }


    /**
     * 修改用户密码
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/alterPassword")
    public Map<String, Object> updataPassword(HttpSession session, @RequestParam("oldPass")String oldPass,
                                              @RequestParam("newPass")String newPass){
        Map<String, Object> map = new HashMap<>();
        User user = (User)session.getAttribute("currentUser");
        String oldSaltPass = user.getPassword();
        String importOldPass = PasswordHelper.encryptPassword(oldPass,user.getSalt());
        if (oldSaltPass.equals(importOldPass)){
            String newSaltPass = PasswordHelper.encryptPassword(newPass, user.getSalt());
            user.setPassword(newSaltPass);

            boolean userBlags = userService.updataUser(user);
            if (userBlags){
                map.put("code", 0);
                map.put("success", true);
                map.put("msg", "用户修改密码成功");
            }else{
                map.put("code", 1);
                map.put("success", false);
                map.put("msg", "用户修改密码失败");
            }
        }else {
            map.put("code", 2);
            map.put("success", false);
            map.put("msg", "输入的原密码不正确");
        }
        return map;
    }

    /**
     * 找到当前正在操作的用户
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/findOne")
    public Map<String, Object> findUser(HttpSession session){
        Map<String, Object> map = new HashMap<>();
        try {
            User currentUser = (User)session.getAttribute("currentUser");
            String schoolName = "";
            if (currentUser.getSchoolId()!=null){
                School daoOne = schoolDao.getOne(currentUser.getSchoolId());
                schoolName = daoOne.getFullName();
            }
            map.put("code", 0);
            map.put("data", currentUser);
            map.put("schoolName", schoolName);
            map.put("success", true);
            map.put("msg", "查看个人资料成功");
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", 500);
            map.put("success", false);
        }
        return map;
    }


    /**
     * 更新用户信息，将用户更改成用过验证
     * @param session
     * @param emailVal
     * @return
     */
    @ResponseBody
    @PutMapping(value = "/updateEmail")
    public Map<String, Object> updateEmail(HttpSession session, @RequestParam("eV")String emailVal){
        Map<String, Object> map = new HashMap<>();
        User currentUser = (User)session.getAttribute("currentUser");
        String reEmail = currentUser.getEmail();
        if (reEmail.equals(emailVal)){
            currentUser.setStatus(2);
            boolean userBlags = userService.updataUser(currentUser);
            if (userBlags){
                map.put("code", 0);
                map.put("success", true);
                map.put("msg", "更新用户信息成功");
            }else{
                map.put("code", 0);
                map.put("success", true);
                map.put("msg", "更新用户信息失败，服务出错");
            }
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "输入的用户邮箱与注册邮箱不符合");
        }
        return map;
    }


    /**
     * 更改信息时，进行页面跳转
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateInfo/{userId}")
    public String updateInfo(@PathVariable Integer userId, Model model){
        User user = userDao.getOne(userId);
        model.addAttribute("user", user);
        return "user/updateInfo";
    }


    /**
     * 点击更新，更新个人信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBtn")
    public Integer updateBtn(User user){
        User userDaoOne = userDao.getOne(user.getId());
        userDaoOne.setPhone(user.getPhone());
        userDaoOne.setWechat(user.getWechat());
        userDaoOne.setQq(user.getQq());
        userDaoOne.setSignature(user.getSignature());
        userDaoOne.setAddress(user.getAddress());
        try{
            userDao.saveAndFlush(userDaoOne);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


}





