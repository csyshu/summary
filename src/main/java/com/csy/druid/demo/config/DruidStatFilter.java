package com.csy.druid.demo.config;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author csy
 * description druid过滤器
 * @date create in 16:32 2019/9/6
 */
// @WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
//         initParams = {
//                 @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")//忽略资源
//         }
// )
public class DruidStatFilter extends WebStatFilter {

}
