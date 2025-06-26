package com.ren.quartz.domain.job;

import org.quartz.JobExecutionContext;

import com.ren.quartz.domain.entity.TimedTask;
import com.ren.quartz.utils.JobInvokeUtil;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author ren
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    /**
     * 通过反射执行定时任务
     * 
     * @param context Quartz任务执行上下文
     * @param timedTask 任务执行所需参数
     * @author ren
     * @date 2025/06/24 10:09
     */
    @Override
    protected void doExecute(JobExecutionContext context, TimedTask timedTask) throws Exception {
        // 通过反射调用对应类的对应方法
        JobInvokeUtil.invokeMethod(timedTask);
    }
}
