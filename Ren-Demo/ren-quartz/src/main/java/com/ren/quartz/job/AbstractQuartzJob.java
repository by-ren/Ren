package com.ren.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.ren.common.core.constant.AppConstants;
import com.ren.common.manager.SpringManager;
import com.ren.common.utils.BeanUtils;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.ExceptionUtils;
import com.ren.common.utils.StringUtils;
import com.ren.quartz.core.constant.QuartzContents;
import com.ren.quartz.core.domain.entity.TimedTask;
import com.ren.quartz.core.domain.entity.TimedTaskLog;
import com.ren.quartz.service.TimedTaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractQuartzJob implements Job {
    /**
     * 线程本地变量
     */
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        // 创建一个新的参数对象用于任务的执行
        TimedTask timedTask = new TimedTask();

        // 从Quartz的JobDataMap中获取任务属性信息（JobDataMap是Quartz中存储任务参数的容器）
        // 使用BeanUtils.copyBeanProp将属性复制到新建的timedTask对象
        // 这里为什么需要复制，而不是直接进行强转。是因为
        // 1.Quartz的JobDataMap中存储的是任务初始化时的原始对象，如果直接重用这个对象，上一次执行的状态数据会影响下一次执行，并且并发执行时会出现线程安全问题
        // 2.get()方法返回的是Object类型，如果存储的对象类型不匹配，会抛出异常
        BeanUtils.copyBeanProp(context.getMergedJobDataMap().get(QuartzContents.TASK_PROPERTIES), timedTask);
        try {
            // 执行前
            before(context, timedTask);
            if (timedTask != null) {
                // 执行方法
                doExecute(context, timedTask);
            }
            // 执行后
            after(context, timedTask, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, timedTask, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param timedTask 系统计划任务
     */
    protected void before(JobExecutionContext context, TimedTask timedTask) {
        // 设置开始时间，方便计算任务执行耗时
        threadLocal.set(DateUtils.currentSeconds());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param timedTask 系统计划任务
     */
    protected void after(JobExecutionContext context, TimedTask timedTask, Exception e) {
        // 获取开始时间
        Long startTime = threadLocal.get();
        // 删除线程变量
        threadLocal.remove();

        // 添加任务执行日志
        final TimedTaskLog timedTaskLog = new TimedTaskLog();
        timedTaskLog.setTaskName(timedTask.getTaskName());
        timedTaskLog.setTaskGroup(timedTask.getTaskGroup());
        timedTaskLog.setInvokeTarget(timedTask.getInvokeTarget());
        timedTaskLog.setStartTime(startTime);
        timedTaskLog.setStopTime(DateUtils.currentSeconds());
        // 计算任务耗时
        long runMs = (timedTaskLog.getStopTime() - timedTaskLog.getStartTime()) * 1000;
        timedTaskLog.setTaskMessage(timedTaskLog.getTaskName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            // 如果执行中有报错，则截取报错信息，并将任务执行状态设置为失败
            timedTaskLog.setStatus(AppConstants.TIMED_TASK_LOG_STATUS_FAIL);
            // 日志存入数据库，所以这里获取详细信息存储比较好，方便排查问题
            String errorMsg = StringUtils.sub(ExceptionUtils.getExceptionMessage(e), 0, 2000);
            timedTaskLog.setExceptionInfo(errorMsg);
        } else {
            timedTaskLog.setStatus(AppConstants.TIMED_TASK_LOG_STATUS_NORMAL);
        }

        // 添加任务执行日志
        SpringManager.getBean(TimedTaskService.class).addTimedTaskLog(timedTaskLog);
    }

    /**
     * 执行方法，由子类重载（抽象方法）
     *
     * @param context 工作执行上下文对象
     * @param timedTask 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, TimedTask timedTask) throws Exception;
}