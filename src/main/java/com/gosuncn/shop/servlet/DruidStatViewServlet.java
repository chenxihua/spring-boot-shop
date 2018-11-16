package com.gosuncn.shop.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author: chenxihua
 * @Date: 2018-11-16:10:29
 *
 * 监控视图配置
 */

@WebServlet(urlPatterns = "/druid/*", initParams = {
        @WebInitParam(name = "allow", value = ""), // IP白名单 (没有配置或者为空，则允许所有访问)
        @WebInitParam(name = "deny", value = "193.112.71.80"),// IP黑名单 (存在共同时，deny优先于allow)
        @WebInitParam(name = "loginUsername", value = "chenxihua"),
        @WebInitParam(name = "loginPassword", value = "123456"),
        @WebInitParam(name = "resetEnable", value = "true") // 禁用HTML页面上的“Reset All”功能
})
public class DruidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = 2359758657306626394L;
}



