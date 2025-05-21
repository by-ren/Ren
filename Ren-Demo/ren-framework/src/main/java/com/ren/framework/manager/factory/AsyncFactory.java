package com.ren.framework.manager.factory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.ren.common.domain.entity.Logininfor;
import com.ren.common.domain.entity.OperLog;
import com.ren.common.domain.entity.User;
import com.ren.common.utils.ServletUtils;
import com.ren.common.utils.SpringUtils;
import com.ren.common.utils.ip.AddressUtils;
import com.ren.common.utils.ip.IpUtils;
import com.ren.system.service.LogininforService;
import com.ren.system.service.OperLogService;
import com.ren.system.service.UserService;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * 
 * @author admin
 */
public class AsyncFactory
{

    public static TimerTask addLogininfor(final String username, final Byte isSuccess, final String message)
    {
        String userAgentStr = ServletUtils.getRequest().getHeader("User-Agent");
        // 解析User-Agent
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);

        // 获取浏览器类型和浏览器版本
        String browser = userAgent.getBrowser().getName();
        String browserVersion = userAgent.getVersion();

        // 获取操作系统和操作系统版本
        String os = userAgent.getOs().getName();
        String osVersion = userAgent.getOsVersion();

        // 获取客户端IP地址
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

        return new TimerTask()
        {
            @Override
            public void run()
            {
                Logininfor logininfor = new Logininfor();
                logininfor.setIsSuccess(isSuccess);
                logininfor.setMsg(message);
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
                logininfor.setLoginTime(DateUtil.currentSeconds());
                SpringUtils.getBean(LogininforService.class).addLogininfor(logininfor);

                //更新最后登录时间
                UserService userService = SpringUtils.getBean(UserService.class);
                User loginIpUser = userService.getUserByUsername(username);
                userService.modifyUserByLogin(loginIpUser.getUserId(),ip,DateUtil.currentSeconds(),username);
            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask addOperLog(final OperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(OperLogService.class).addOperLog(operLog);
            }
        };
    }
}
