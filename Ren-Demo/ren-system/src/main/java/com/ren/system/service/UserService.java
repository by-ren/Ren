package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.User;

import java.util.List;

//IService是MyBatis-Plus提供的一个通用接口，继承了IService接口，可以自动生成一些通用方法，如：查询、新增、修改、删除等。
public interface UserService extends IService<User> {

    /*
     * 添加用户详情
     * @param user
     * @author admin
     * @date 2025/04/16 16:24
     */
    void saveUserDetails(User user);

    /*
     * 根据用户名查询User
     * @param userName
     * @return com.ren.entity.User
     * @author admin
     * @date 2025/04/17 14:53
     */
    User getUserByUsername(String userName);

    /*
     * 根据ID查询User
     * @param id
     * @return com.ren.entity.User
     * @author admin
     * @date 2025/04/17 15:44
     */
    User getUserById(Long id);

    /*
     * 获取用户列表
     * @return java.util.List<com.ren.system.entity.User>
     * @author admin
     * @date 2025/04/26 15:51
     */
    List<User> listUserByParam();

}