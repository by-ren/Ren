
package com.ren.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.quartz.core.domain.entity.TimedTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TimedTaskMapper extends BaseMapper<TimedTask> {

    /**
     * 添加定时任务
     * 
     * @param timedTask
     * @author ren
     * @date 2025/05/07 17:38
     */
    int insertTimedTask(TimedTask timedTask);

    /**
     * 编辑定时任务状态
     * 
     * @param taskId
     * @param updateBy
     * @param updateTime
     * @author ren
     * @date 2025/05/07 17:30
     */
    void updateTimedTaskStatusById(@Param("taskId") long taskId, @Param("status") byte status,
        @Param("updateBy") String updateBy, @Param("updateTime") long updateTime);

    /**
     * 编辑定时任务
     * 
     * @param timedTask
     * @author ren
     * @date 2025/05/07 17:31
     */
    int updateTimedTaskById(TimedTask timedTask);

    /**
     * 根据参数获取定时任务列表
     * 
     * @param paramMap
     * @author ren
     * @date 2025/05/07 17:31
     */
    IPage<TimedTask> listTimedTaskByPage(Page<TimedTask> page, @Param("paramMap") Map<String, Object> paramMap);

    /**
     * 不分页获取定时任务列表
     * 
     * @param paramMap
     * @return java.util.List<com.ren.quartz.domain.entity.TimedTask>
     * @author ren
     * @date 2025/06/23 15:16
     */
    List<TimedTask> listTimedTaskByParam(Map<String, Object> paramMap);

    /**
     * 根据任务ID删除定时任务
     * 
     * @param taskId
     * @author ren
     * @date 2025/06/23 15:18
     */
    void deleteTimedTaskById(long taskId);
}