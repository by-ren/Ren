package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.domain.entity.User;
import com.ren.common.utils.PageUtils;
import com.ren.system.entity.UserRole;
import com.ren.system.mapper.UserMapper;
import com.ren.system.mapper.UserRoleMapper;
import com.ren.system.service.UserService;
import com.ren.common.constant.AppConstants;
import com.ren.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//ServiceImpl是Mybatis-Plus提供的一个针对IService的具体实现类
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //自动注入密码管理器
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    /*
     * 添加用户详情
     * @param user
     * @author admin
     * @date 2025/04/16 16:24
     */
    @Override
    @Transactional
    public long addUser(User user,String createBy) {
        // 使用设定的密码管理器对密码进行编码(设置默认密码123456)
        String encodedPassword = SecurityUtils.encryptPassword("123456");
        user.setIsDel(AppConstants.COMMON_BYTE_NO);
        user.setUserType(AppConstants.USER_USERTYPE_SYSTEM);
        user.setCreateBy(createBy);
        user.setCreateTime(DateUtil.currentSeconds());
        user.setPassword(encodedPassword);
        userMapper.insertUser(user);
        //添加角色
        if(user.getRoleIdArr() != null && user.getRoleIdArr().length > 0){
            List<UserRole> userRoleList = Arrays.stream(user.getRoleIdArr()).map(roleId -> new UserRole(user.getUserId(),roleId)).toList();
            userRoleMapper.insertUserRoleBatch(userRoleList);
        }
        return user.getUserId();
    }

    /*
     * 编辑用户是否删除
     * @param userId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/04 13:59
     */
    @Override
    public void modifyUserIsDelById(long userId, byte isDel, String updateBy) {
        userMapper.updateUserIsDelById(userId,isDel,updateBy,DateUtil.currentSeconds());
    }

    /*
     * 重置密码（后台使用）
     * @param userId
     * @param newPassword
     * @param updateBy
     * @author admin
     * @date 2025/05/04 13:38
     */
    @Override
    public void resetPassword(long userId, String newPassword, String updateBy) {
        if(StrUtil.isNotBlank(newPassword)){
            newPassword = SecurityUtils.encryptPassword(newPassword);
        }
        userMapper.resetPassword(userId,newPassword,updateBy,DateUtil.currentSeconds());
    }

    /*
     * 编辑用户
     * @param user
     * @param updateBy
     * @author admin
     * @date 2025/05/04 13:52
     */
    @Override
    public void modifyUser(User user, String updateBy) {
        user.setUpdateBy(updateBy);
        user.setUpdateTime(DateUtil.currentSeconds());
        userMapper.updateUser(user);
        //先删除角色
        userRoleMapper.deleteUserRoleByUserId(user.getUserId());
        //重新添加角色
        if(user.getRoleIdArr() != null && user.getRoleIdArr().length > 0){
            List<UserRole> userRoleList = Arrays.stream(user.getRoleIdArr()).map(roleId -> new UserRole(user.getUserId(),roleId)).toList();
            userRoleMapper.insertUserRoleBatch(userRoleList);
        }
    }

    /*
     * 编辑用户（登录用）
     * @param userId
     * @param loginIp
     * @param loginDate
     * @param updateBy
     * @author admin
     * @date 2025/05/16 16:08
     */
    @Override
    public void modifyUserByLogin(long userId, String loginIp, long loginDate, String updateBy) {
        userMapper.updateUserByLogin(userId,loginIp,loginDate,updateBy,DateUtil.currentSeconds());
    }

    /*
     * 根据登陆账号获取username
     * @param username
     * @return com.ren.admin.entity.User
     * @author admin
     * @date 2025/05/04 17:27
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByParam(Map.of("username",username));
    }

    /*
     * 根据ID查询User
     * @param id
     * @return com.ren.entity.User
     * @author admin
     * @date 2025/04/17 15:44
     */
    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectById(id);
        return user;
    }

    /*
     * 获取用户列表
     * @return java.util.List<com.ren.admin.entity.User>
     * @author admin
     * @date 2025/04/26 15:52
     */
    @Override
    public IPage<User> listUserByPage(Map<String,Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        return userMapper.listUserByPage(PageUtils.createPage(User.class),paramMap);
    }

    /*
     * 获取用户列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.User>
     * @author admin
     * @date 2025/05/22 17:07
     */
    @Override
    public List<User> listUserByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        return userMapper.listUserByParam(paramMap);
    }

    /*
     * 获取用户列表
     * @param roleId
     * @return java.util.List<com.ren.common.core.entity.User>
     * @author admin
     * @date 2025/05/13 10:10
     */
    @Override
    public List<User> listUserByRoleId(long roleId) {
        List<User> userList = userMapper.listUserByRoleId(roleId);
        return userList;
    }

    /*
     * 获取用户列表
     * @param deptId
     * @return java.util.List<com.ren.common.core.entity.User>
     * @author admin
     * @date 2025/05/13 10:10
     */
    @Override
    public List<User> listUserByDeptId(long deptId) {
        List<User> userList = userMapper.listUserByDeptId(deptId);
        return userList;
    }

}