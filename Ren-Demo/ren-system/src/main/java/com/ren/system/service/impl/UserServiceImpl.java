package com.ren.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.system.security.config.DBUserDetailsManager;
import com.ren.system.entity.User;
import com.ren.system.mapper.UserMapper;
import com.ren.system.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//ServiceImpl是Mybatis-Plus提供的一个针对IService的具体实现类
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //自动注入数据库用户信息管理器
    @Resource
    private DBUserDetailsManager dbUserDetailsManager;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder; // 注入编码器

    /*
     * 添加用户详情
     * @param user
     * @author admin
     * @date 2025/04/16 16:24
     */
    @Override
    public void saveUserDetails(User user) {
        // 使用设定的密码管理器对密码进行编码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //将我们自己的用户，封装成一个UserDetails对象
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername()) //自定义用户名
                .password(user.getPassword()) //自定义密码
                .build();
        //调用数据库用户信息管理器中的创建用户方法，在这个方法中进行数据库的插入操作
        dbUserDetailsManager.createUser(userDetails);
    }

    /*
     * 根据用户名查询User
     * @param userName
     * @return com.ren.entity.User
     * @author admin
     * @date 2025/04/17 14:53
     */
    @Override
    public User getUserByUsername(String userName) {
        /**
         * QueryWrapper是MyBatis-Plus中的查询条件构造器，可以使用eq方法添加查询对象，eq表示使用等于号链接列和名
         * 使用MyBatis-Plus的selectOne方法查询数据库，返回一个User对象
         * **/
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        User user = userMapper.selectOne(queryWrapper);

        return user;
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

}