package com.gosuncn.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//这行是为了避免扫描不到Druid的Servlet
@ServletComponentScan
public class SpringBootShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShopApplication.class, args);
    }
}
