package com.csy.summary.daily.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csy.summary.daily.beans.ReturnBean;
import com.csy.summary.daily.beans.User;
import com.csy.summary.daily.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuyun.cheng
 * @since 2019-01-30
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 用户数据访问接口
     */
    @Resource
    private UserMapper userMapper;

    /**
     * @param user
     * @return
     * @throws Exception
     * @Title: addUser
     * @Description: 新增用户
     */
    public ReturnBean addUser(User user) throws Exception {
        log.info("====>>执行addUser方法");
        ReturnBean returnBean = new ReturnBean();
        // 判断参数
        if (user == null) {
            returnBean.setFailReturn("传入的参数不合法");
            return returnBean;
        }
        // 添加的结果
        int resultNum = 0;
        try {
            // 这里的insert是BaseMapper中的方法
            resultNum = userMapper.insert(user);
        } catch (Exception e) {
            log.error("依据id查找用户时发生异常", e);
            returnBean.setFailReturn("程序内部发生错误");
            return returnBean;
        }
        log.info("添加用户数:" + resultNum);
        if (resultNum == 0) {
            // 添加失败
            returnBean.setFailReturn("添加用户失败");
        } else {
            returnBean.setSuccessReturn("添加用户成功");
        }
        return returnBean;
    }

    /**
     * @param userId
     * @return
     * @throws Exception
     * @Title: selectUserById
     * @Description: 依据id查找用户
     */
    public ReturnBean selectUserById(String userId) throws Exception {
        log.info("====>>执行selectUserById方法");
        ReturnBean returnBean = new ReturnBean();
        // 判断参数是否合法
        if (userId == null || "".equals(userId)) {
            returnBean.setFailReturn("传入的参数不合法");
            return returnBean;
        }
        // 查询
        User user = null;
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("id", userId);
        try {
            user = userMapper.selectUserByMap(parameterMap);
        } catch (Exception e) {
            log.error("依据id查找用户时发生异常", e);
            returnBean.setFailReturn("程序内部发生错误");
            return returnBean;
        }
        if (user != null) {
            // 查找到了用户信息
            returnBean.setSuccessReturn(user);
        } else {
            returnBean.setFailReturn("没有查找到此用户信息");
        }
        return returnBean;
    }

}
