package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.common.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 添加用户
     * @param user
     * @author ren
     * @date 2025/05/04 13:30
     */
    void insertUser(User user);

    /**
     * 编辑用户是否删除
     * @param userId
     * @param isDel
     * @param updateBy
     * @param updateTime
     * @author ren
     * @date 2025/05/04 13:59
     */
    void updateUserIsDelById(@Param("userId")long userId, @Param("isDel")byte isDel, @Param("updateBy")String updateBy,@Param("updateTime")long updateTime);

    /**
     * 重置密码（后台使用）
     * @param userId
     * @param newPassword
     * @param updateBy
     * @param updateTime
     * @author ren
     * @date 2025/05/04 13:50
     */
    void resetPassword(@Param("userId")long userId, @Param("newPassword")String newPassword, @Param("updateBy")String updateBy,@Param("updateTime")long updateTime);

    /**
     * 编辑用户
     * @param user
     * @author ren
     * @date 2025/05/04 13:52
     */
    void updateUser(User user);

    /**
     * 编辑用户（登录用）
     * @param userId
     * @param loginIp
     * @param loginDate
     * @param updateBy
     * @author ren
     * @date 2025/05/16 16:08
     */
    void updateUserByLogin(@Param("userId") long userId, @Param("loginIp")String loginIp, @Param("loginDate")long loginDate, @Param("updateBy")String updateBy, @Param("updateTime")long updateTime);

    /**
     * 获取用户列表
     * @param page
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.common.domain.entity.User>
     * @author ren
     * @date 2025/05/13 15:43
     */
    IPage<User> listUserByPage(Page<User> page, @Param("paramMap") Map<String,Object> paramMap);

    /**
     * 获取用户列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.User>
     * @author ren
     * @date 2025/05/22 17:08
     */
    List<User> listUserByParam(Map<String, Object> paramMap);

    /**
     * 根据查询参数获取用户列表
     * @param paramMap
     * @return com.ren.admin.entity.User
     * @author ren
     * @date 2025/05/04 13:28
     */
    User selectUserByParam(Map<String,Object> paramMap);

    /**
     * 获取用户列表
     * @param roleId
     * @return java.util.List<com.ren.common.core.entity.User>
     * @author ren
     * @date 2025/05/13 10:11
     */
    List<User> listUserByRoleId(long roleId);

    /**
     * 获取用户列表
     * @param deptId
     * @return java.util.List<com.ren.common.core.entity.User>
     * @author ren
     * @date 2025/05/13 10:10
     */
    List<User> listUserByDeptId(long deptId);

}