package com.gosuncn.shop.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.gosuncn.shop.shiro.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: chenxihua
 * @Date: 2018-11-22:14:30
// */
@Configuration
public class shiroConfig {

    /**
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> map = new HashMap<String, String>();

        map.put("/layui/**", "anon");
        map.put("/biz/**", "anon");
        map.put("/res/**", "anon");
        map.put("/statics/**", "anon");
        map.put("/favicon.ico", "anon");
        map.put("/images/**", "anon");
        map.put("/", "anon");
        map.put("/index.html", "anon");
        map.put("/common/register", "anon");
        map.put("/common/applySchool", "anon");
        map.put("/school/getSchools", "anon");
        map.put("/school/getPartions", "anon");
        map.put("/user/accountVerify", "anon");
        map.put("/user/emailVerify", "anon");
        map.put("/user/loginSyst", "anon");
        map.put("/school/saveSchool", "anon");
        map.put("/user/saveByRegister", "anon");
        // 找回密码的链接
        map.put("/common/findPassword","anon");

        // 管理员登录页面
        map.put("/common/admin", "anon");

        map.put("/common/firstIndex", "user");
        map.put("/druid/**", "user");
        // 登出
        map.put("/common/logout", "logout");
        // 对所有用户进行验证
        map.put("/**", "user");

        // 登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 验证成功的首页
        shiroFilterFactoryBean.setSuccessUrl("/common/loginIndex");
        // 错误页面，认证不通过，才跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        // 设置 realm
//        securityManager.setAuthenticator(modularRealmAuthenticator());
//        List<Realm> realms = new ArrayList<>();
//        // 添加多个realm
//        realms.add(userRealm());
//        realms.add(adminsRealm());
//        securityManager.setRealms(realms);

        return securityManager;
    }

    /**
     * 系统自带的 Realm管理器， 主要针对多Realm
     * @return
     */
//    @Bean
//    public ModularRealmAuthenticator modularRealmAuthenticator(){
//        // 自己重写的ModularRealmAuthenticator
//        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
//        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//        return modularRealmAuthenticator;
//    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

//    @Bean
//    public AdminsRealm adminsRealm(){
//        AdminsRealm adminsRealm = new AdminsRealm();
//        adminsRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return adminsRealm;
//    }

    /** 启动shiro在ioc容器中的注解，只有在使用
     * 开启 Shiro 的注解功能 (如 @RequiresRoles,@RequiresPermissions),需借助 SpringAOP 扫描使 用 Shiro 注解的类 ,
     * 并在必要时进行安全逻辑验证,需要配置两个bean(DefaultAdvisorAutoProxyCreator(可选) 和
     * AuthorizationAttributeSourceAdvisor) 实现此功能。Spring Boot系列安全框架Apache Shiro基本功能
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 开启shiro aop注解支持, 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * thymeleaf的shiro标签库
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }



}
