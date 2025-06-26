package com.ren.quartz.service;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.quartz.domain.entity.TimedTask;
import com.ren.quartz.domain.entity.TimedTaskLog;
import com.ren.quartz.domain.exception.QuartzException;

public interface TimedTaskService extends IService<TimedTask> {

    /*===================================================TimedTask====================================================*/

    /**
     * 添加定时任务
     * 
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    long addTimedTask(TimedTask timedTask, String createBy) throws SchedulerException, QuartzException;

    /**
     * 编辑定时任务是否删除
     * 
     * @param taskId
     * @param status
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void modifyTimedTaskStatusById(long taskId, byte status, String updateBy) throws SchedulerException;

    /**
     * 编辑定时任务
     * 
     * @param timedTask
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void modifyTimedTaskById(TimedTask timedTask, String updateBy) throws SchedulerException, QuartzException;

    /**
     * 获取定时任务详情
     * 
     * @param taskId
     * @return com.ren.common.core.entity.TimedTask
     * @author ren
     * @date 2025/05/07 17:14
     */
    TimedTask getTimedTaskById(long taskId);

    /**
     * 根据参数获取定时任务列表
     * 
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.TimedTask>
     * @author ren
     * @date 2025/05/07 17:15
     */
    IPage<TimedTask> listTimedTaskByPage(Map<String, Object> paramMap);

    /**
     * 不分页获取定时任务列表
     * 
     * @param paramMap
     * @return java.util.List<com.ren.quartz.domain.entity.TimedTask>
     * @author ren
     * @date 2025/06/23 15:15
     */
    List<TimedTask> listTimedTaskByParam(Map<String, Object> paramMap);

    /**
     * 根据任务ID删除定时任务
     * 
     * @param taskId
     * @author ren
     * @date 2025/06/23 15:17
     */
    void removeTimedTaskById(long taskId) throws SchedulerException;

    /*===================================================TimedTaskLog=================================================*/

    /**
     * 添加定时任务日志
     * 
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    long addTimedTaskLog(TimedTaskLog timedTaskLog);

    /**
     * 根据参数获取定时任务日志列表
     * 
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.TimedTaskLog>
     * @author ren
     * @date 2025/05/07 17:15
     */
    List<TimedTaskLog> listTimedTaskLogByParam(Map<String, Object> paramMap);

}