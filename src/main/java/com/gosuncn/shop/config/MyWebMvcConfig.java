package com.gosuncn.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

        };
        return webMvcConfigurer;
    }

}
