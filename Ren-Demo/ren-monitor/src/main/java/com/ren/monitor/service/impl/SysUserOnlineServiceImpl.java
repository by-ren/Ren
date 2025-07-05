package com.ren.monitor.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.utils.DateUtils;
import com.ren.monitor.core.domain.vo.SysUserOnlineVO;
import com.ren.monitor.service.SysUserOnlineService;
import org.springframework.stereotype.Service;

/**
 * 在线用户 服务层处理
 * 
 * @author ren
 */
@Service
public class SysUserOnlineServiceImpl implements SysUserOnlineService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnlineVO selectOnlineByIpaddr(String ipaddr, LoginUser user, String refreshToken)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return selectOnline(user, refreshToken);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnlineVO selectOnlineByUserName(String userName, LoginUser user, String refreshToken)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return selectOnline(user, refreshToken);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnlineVO selectOnlineByInfo(String ipaddr, String userName, LoginUser user, String refreshToken)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return selectOnline(user, refreshToken);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnlineVO selectOnline(LoginUser user, String refreshToken)
    {
        if (ObjUtil.isNull(user) || ObjUtil.isNull(user.getUser()))
        {
            return null;
        }
        SysUserOnlineVO sysUserOnline = new SysUserOnlineVO();
        sysUserOnline.setTokenId(refreshToken);
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(DateUtils.timestampToStrDefault(user.getLoginTime() * 1000L));
        if (ObjUtil.isNotNull(user.getUser().getDept()))
        {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        return sysUserOnline;
    }
}
