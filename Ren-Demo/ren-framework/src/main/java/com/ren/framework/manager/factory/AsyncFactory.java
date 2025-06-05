package com.ren.framework.manager.factory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.ren.common.constant.AppConstants;
import com.ren.common.domain.entity.Logininfor;
import com.ren.common.domain.entity.OperLog;
import com.ren.common.domain.entity.User;
import com.ren.common.utils.ServletUtils;
import com.ren.common.utils.SpringUtils;
import com.ren.common.utils.ip.AddressUtils;
import com.ren.common.utils.ip.IpUtils;
import com.ren.common.properties.MailProperties;
import com.ren.system.entity.Notice;
import com.ren.system.service.LogininforService;
import com.ren.system.service.OperLogService;
import com.ren.system.service.UserService;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * 
 * @author ren
 */
public class AsyncFactory
{

    /*
     * 添加登录日志
     * @param username
     * @param isSuccess
     * @param message
     * @return java.util.TimerTask
     * @author ren
     * @date 2025/05/23 11:19
     */
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
        // 注入UserService
        UserService userService = SpringUtils.getBean(UserService.class);
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
        // 注入OperLogService
        OperLogService operLogService = SpringUtils.getBean(OperLogService.class);
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                operLogService.addOperLog(operLog);
            }
        };
    }

    /*
     * 异步发送邮件
     * @param notice
     * @return java.util.TimerTask
     * @author ren
     * @date 2025/05/23 11:22
     */
    public static TimerTask sendAddOrModifyNoticeEmail(final Notice notice,final String title)
    {
        UserService userService = SpringUtils.getBean(UserService.class);
        //读取Mail配置文件
        MailProperties mailProperties = SpringUtils.getBean(MailProperties.class);
        // Spring 自动注入模板引擎（SpringBoot会自动按照所引入的Jar包（Velocity、Beetl、Freemarker、Thymeleaf），以及所配置的配置文件（如thymeleaf.yml）进行自动模板引擎注入）
        ITemplateEngine templateEngine = SpringUtils.getBean(ITemplateEngine.class);
        return new TimerTask()
        {
            @Override
            public void run()
            {
                //获取所有用户列表，发送邮件
                List<User> userList = userService.listUserByParam(null);
                // 手动创建MailAccount对象，读取自定义的配置文件
                MailAccount account = new MailAccount();
                account.setHost(mailProperties.getHost());
                account.setPort(mailProperties.getPort());
                account.setUser(mailProperties.getUsername());
                account.setPass(mailProperties.getPassword());
                account.setFrom(mailProperties.getFrom());
                account.setSslEnable(mailProperties.isSslEnable());
                // 渲染模板
                Context context = new Context();
                context.setVariable("noticeTitle", notice.getNoticeTitle());
                context.setVariable("noticeType", notice.getNoticeType() == AppConstants.NOTICE_TYPE_NOTICE ? "通知":"公告");
                context.setVariable("noticeContent", notice.getNoticeContent());
                context.setVariable("systemName", "Ren系统管理员");
                String html = templateEngine.process("noticeMail", context); // 对应 noticeMail.html
                //实现发送（群发）
                List<String> toList = userList.stream().map(User::getEmail).toList();
                MailUtil.send(account, toList, title, html, true);
            }
        };
    }
}