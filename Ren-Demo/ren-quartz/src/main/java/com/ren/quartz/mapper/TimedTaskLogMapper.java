
package com.ren.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.quartz.core.domain.entity.TimedTaskLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TimedTaskLogMapper extends BaseMapper<TimedTaskLog> {

    /**
     * 添加定时任务日志
     * @param timedTaskLog
     * @author ren
     * @date 2025/05/07 17:38
     */
    void insertTimedTaskLog(TimedTaskLog timedTaskLog);

    /**
     * 根据参数获取定时任务日志列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.TimedTaskLog>
     * @author ren
     * @date 2025/05/07 17:31
     */
    List<TimedTaskLog> listTimedTaskLogByParam(Map<String, Object> paramMap);

}