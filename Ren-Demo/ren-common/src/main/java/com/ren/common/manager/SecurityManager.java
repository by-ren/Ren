package com.ren.common.manager;

import com.ren.common.core.constant.HttpStatus;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全服务工具类（由于不是纯静态方法，参与了业务逻辑的计算，所以设计为Manager模块，而不是Utils模块）
 * 
 * @author ren
 */
public class SecurityManager
{

    /**
     * 用户ID
     **/
    public static Long getUserId()
    {
        try
        {
            return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId()
    {
        try
        {
            return getLoginUser().getUser().getDeptId();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取部门ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        try
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //使用多密码管理器对密码进行加密（加密后会带有前缀）（默认使用BCryptPasswordEncoder密码管理器）(SpringSecurity已经配置了PasswordEncoder，所以这里获取Bean使用)
        PasswordEncoder passwordEncoder = SpringManager.getBean(PasswordEncoder.class);
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //使用多密码管理器对密码进行加密（加密后会带有前缀）（默认使用BCryptPasswordEncoder密码管理器）(SpringSecurity已经配置了PasswordEncoder，所以这里获取Bean使用)
        PasswordEncoder passwordEncoder = SpringManager.getBean(PasswordEncoder.class);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

}