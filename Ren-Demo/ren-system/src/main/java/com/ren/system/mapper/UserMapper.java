package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.system.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /*
     * 获取用户列表
     * @return java.util.List<com.ren.system.entity.User>
     * @author admin
     * @date 2025/04/26 15:53
     */
    List<User> listUserByParam(Map<String,Object> paramMap);
}