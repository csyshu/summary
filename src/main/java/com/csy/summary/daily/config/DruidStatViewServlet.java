package com.csy.summary.daily.config;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author csy
 * description druid数据源状态监控.
 * @date create in 16:28 2019/9/6
 */
// @WebServlet(urlPatterns = "/druid/*",
//         initParams = {
//                 @WebInitParam(name = "allow", value = "172.16.87.120"),// IP白名单(没有配置或者为空，则允许所有访问)
//                 // @WebInitParam(name = "deny", value = "127.0.0.1"),// IP黑名单 (存在共同时，deny优先于allow)
//                 @WebInitParam(name = "loginUsername", value = "druid"),// 用户名
//                 @WebInitParam(name = "loginPassword", value = "druid"),// 密码
//                 @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
//         }
// )
public class DruidStatViewServlet extends StatViewServlet {

}
