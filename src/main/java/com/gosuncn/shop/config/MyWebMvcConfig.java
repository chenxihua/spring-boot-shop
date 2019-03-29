package com.gosuncn.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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
                registry.addViewController("/logout").setViewName("index");
            }

            @Bean(name = "multipartResolver")
            public CommonsMultipartResolver commonsMultipartResolver(){
                CommonsMultipartResolver resolver = new CommonsMultipartResolver();
                resolver.setDefaultEncoding("UTF-8");
                // 设定文件上传的最大值为50MB，50*1024*1024
                resolver.setMaxUploadSize(52428800);
                // resolver.setMaxInMemorySize();
                // 延迟文件解析
                resolver.setResolveLazily(true);
                return resolver;
            }

        };


        return webMvcConfigurer;
    }

}
