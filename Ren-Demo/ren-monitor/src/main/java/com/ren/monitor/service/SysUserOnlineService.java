package com.ren.monitor.service;

import com.ren.common.domain.model.bo.LoginUser;
import com.ren.monitor.domain.vo.SysUserOnlineVO;

/**
 * 在线用户 服务层
 * 
 * @author ren
 */
public interface SysUserOnlineService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    public SysUserOnlineVO selectOnlineByIpaddr(String ipaddr, LoginUser user, String refreshToken);

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    public SysUserOnlineVO selectOnlineByUserName(String userName, LoginUser user, String refreshToken);

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    public SysUserOnlineVO selectOnlineByInfo(String ipaddr, String userName, LoginUser user, String refreshToken);

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    public SysUserOnlineVO selectOnline(LoginUser user, String refreshToken);
}
