package com.gosuncn.shop.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author: chenxihua
 * @Date: 2018-11-16:9:49
 */
@Configuration
public class DruidConfig {


    /**
     *  加入到spring容器中，并扫描spring.dataSource前缀的配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }
}
