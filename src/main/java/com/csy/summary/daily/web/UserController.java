package com.csy.summary.daily.web;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csy.summary.daily.beans.ReturnBean;
import com.csy.summary.daily.beans.User;
import com.csy.summary.daily.mapper.UserMapper;
import com.csy.summary.daily.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shuyun.cheng
 * @since 2019-01-30
 */
@RestController
@RequestMapping("/tUser")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @PostMapping(value = "/add")
    public ReturnBean add(@Valid @RequestBody User tUser) {
        boolean insert = userService.save(tUser);
        ReturnBean returnBean = new ReturnBean();
        if (insert) {
            returnBean.setSuccessReturn("新增成功");
        } else {
            returnBean.setFailReturn("新增失败");
        }
        return returnBean;
    }

    @PostMapping(value = "/update")
    public ReturnBean update(@Valid @RequestBody User tUser) {
        boolean insert = userService.updateById(tUser);
        ReturnBean returnBean = new ReturnBean();
        if (insert) {
            returnBean.setSuccessReturn("修改成功");
        } else {
            returnBean.setFailReturn("修改失败");
        }
        return returnBean;
    }

    @GetMapping("/list")
    public String list() {
        IPage<User> userPage = userMapper.selectPage(new Page<>(), new QueryWrapper<User>().eq("name", "csy"));
        return JSON.toJSONString(userPage);
    }
}

