package com.ren.framework.manager.factory;

import com.ren.common.domain.entity.OperLog;
import com.ren.common.utils.SpringUtils;
import com.ren.common.utils.ip.AddressUtils;
import com.ren.system.service.OperLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * 
 * @author admin
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final OperLog operLog)
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
