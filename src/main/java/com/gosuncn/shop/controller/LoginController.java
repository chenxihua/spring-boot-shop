package com.gosuncn.shop.controller;

import com.gosuncn.shop.dao.ProvinceDao;
import com.gosuncn.shop.dao.SchoolDao;
import com.gosuncn.shop.dao.UserDao;
import com.gosuncn.shop.entities.Province;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.service.SchoolService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping(value = "/saveByRegister")
    public Map<String, Object> saveUser(User user){
        Map<String, Object> map =  new HashMap<>();
        String oldPassword = user.getPassword();

        User newUser = PasswordHelper.encryptPassword(oldPassword);
        if (newUser != null){
            user.setPassword(newUser.getPassword());
            user.setSalt(newUser.getSalt());
            user.setStatus(1);
            user.setSignTime(new Date());
            boolean saveFlag = userService.saveOneUser(user);
            if (saveFlag){
                map.put("code", 0);
                map.put("success", true);
                map.put("msg", "你注册用户已成功");
            }else {
                map.put("code", 0);
                map.put("success", false);
                map.put("msg", "你注册用户已失败");
            }
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "注册密码输入值为空");
        }
        return map;
    }


    /**
     * 学生用户登录系统
     * @param emailValue
     * @param passValue
     * @return
     */
    @ResponseBody
    @PostMapping("/loginSyst")
    public Map<String, Object> loginSyst(@RequestParam("emailValue") String emailValue,
                                         @RequestParam("passValue")String passValue){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(emailValue) && StringUtils.isBlank(passValue)){
            log.info("用户名或密码为空! ");
            map.put("code", 0); map.put("success", false); map.put("msg", "用户名或密码为空! ");
            return map;
        }



        Subject currentUser = SecurityUtils.getSubject();
        //验证是否登录成功
        if (!currentUser.isAuthenticated()){
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(emailValue, passValue);
            try {
                // 执行登录.
                currentUser.login(token);
                log.info("登录:-----------> 成功!");
                //把当前用户放入session
                Session session = currentUser.getSession();
                User tUser = userService.findByConditionsToUser(emailValue);
                session.setAttribute("currentUser",tUser);
            }catch(UnknownAccountException uae){
                log.info("对用户[" + emailValue + "]进行登录验证..验证未通过,未知账户");
            }catch(IncorrectCredentialsException ice){
                log.info("对用户[" + emailValue + "]进行登录验证..验证未通过,错误的凭证, 密码不正确");
            }catch (DisabledAccountException sae){
                log.info("对用户[" + emailValue + "]进行登录验证..验证未通过,帐号已经禁止登录");
            }catch(AuthenticationException ae){
                //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
                log.info("对用户[" + emailValue + "]进行登录验证..验证未通过,堆栈轨迹如下");
            }
            map.put("code", 0);
            map.put("success", true);
            map.put("msg", "用户验证成功");
        }else {
            map.put("code", 0);
            map.put("success", false);
            map.put("msg", "用户已经过认证，不需要重新再认证");
        }
        return map;
    }






}





