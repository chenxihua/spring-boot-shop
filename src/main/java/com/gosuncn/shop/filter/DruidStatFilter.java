package com.gosuncn.shop.filter;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author: chenxihua
 * @Date: 2018-11-16:9:56
 *
 * 有人想在这里直接继承StatFilter进行操作，但是这样会导致重复导入，导致无法运行，因为Druid之前已经有将其置入。
 *
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*", initParams = {
        @WebInitParam(name = "exclusions",
                value = "*.js, *.gif, *.jpg, *.bmp, *.png, *.css, *.ico, *.less, *.eot, *.svg, *.ttf, *.woff, /druid/*")
})
public class DruidStatFilter extends WebStatFilter {
}
