package com.gosuncn.shop.config;

import com.gosuncn.shop.interception.LoginInterception;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: chenxihua
 * @Date: 2018-11-14:9:33
 */

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    /**
     * 注入到容器中
     * @return
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("index");
                registry.addViewController("login.html").setViewName("index");
                registry.addViewController("/login").setViewName("index");
            }

            /**
             * 增加自己的拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginInterception()).addPathPatterns("/**")
                        .excludePathPatterns("/login.html","/login","/","/user/loadPartion","/user/loadSchools",
                                "/user/register","/user/submitSchool","/user/savaSchool","/static/**");
            }
        };
        return webMvcConfigurer;
    }

}
