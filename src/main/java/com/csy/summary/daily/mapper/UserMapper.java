package com.csy.summary.daily.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csy.summary.daily.beans.User;

import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author shuyun.cheng
 * @since 2019-01-30
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 多条件组合查找用户
     */
    User selectUserByMap(Map<String, Object> parameterMap) throws Exception;

}
