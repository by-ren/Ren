package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.core.domain.entity.OperLog;

import java.util.Map;

public interface OperLogService extends IService<OperLog> {

    /**
     * 添加操作日志
     * @param operLog
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    long addOperLog(OperLog operLog);

    /**
     * 删除操作日志
     * @param operId
     * @author ren
     * @date 2025/05/18 13:49
     */
    void removeOperLog(long operId);

    /**
     * 分页获取操作日志列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.common.domain.entity.OperLog>
     * @author ren
     * @date 2025/05/18 13:50
     */
    IPage<OperLog> listOperLogByPage(Map<String,Object> paramMap);

    /**
     * 获取操作日志详情
     * @param operId
     * @return com.ren.common.domain.entity.OperLog
     * @author ren
     * @date 2025/05/18 13:50
     */
    OperLog getOperLogById(long operId);
}