package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.core.domain.entity.User;

import java.util.List;
import java.util.Map;

//IService是MyBatis-Plus提供的一个通用接口，继承了IService接口，可以自动生成一些通用方法，如：查询、新增、修改、删除等。
public interface UserService extends IService<User> {

    /**
     * 添加用户详情
     * @param user
     * @author ren
     * @date 2025/04/16 16:24
     */
    long addUser(User user,String createBy);

    /**
     * 编辑用户是否删除
     * @param userId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/04 13:59
     */
    void modifyUserIsDelById(long userId,byte isDel, String updateBy);

    /**
     * 重置密码（后台使用）
     * @param userId
     * @param newPassword
     * @param updateBy
     * @author ren
     * @date 2025/05/04 13:38
     */
    void resetPassword(long userId,String newPassword,String updateBy);

    /**
     * 编辑用户
     * @param user
     * @param updateBy
     * @author ren
     * @date 2025/05/04 13:51
     */
    void modifyUser(User user,String updateBy);

    /**
     * 编辑用户（登录用）
     * @param userId
     * @param loginIp
     * @param loginDate
     * @param updateBy
     * @author ren
     * @date 2025/05/16 16:08
     */
    void modifyUserByLogin(long userId, String loginIp, long loginDate, String updateBy);

    /**
     * 根据登陆账号获取用户
     * @param username
     * @return com.ren.admin.entity.User
     * @author ren
     * @date 2025/05/04 17:27
     */
    User getUserByUsername(String username);

    /**
     * 根据ID查询User
     * @param id
     * @return com.ren.entity.User
     * @author ren
     * @date 2025/04/17 15:44
     */
    User getUserById(Long id);

    /**
     * 获取用户列表
     * @return java.util.List<com.ren.admin.entity.User>
     * @author ren
     * @date 2025/04/26 15:51
     */
    IPage<User> listUserByPage(Map<String,Object> paramMap);

    /**
     * 获取用户列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.User>
     * @author ren
     * @date 2025/05/22 17:07
     */
    List<User> listUserByParam(Map<String,Object> paramMap);

    /**
     * 获取用户列表
     * @param roleId
     * @return java.util.List<com.ren.common.core.entity.User>
     * @author ren
     * @date 2025/05/13 10:10
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