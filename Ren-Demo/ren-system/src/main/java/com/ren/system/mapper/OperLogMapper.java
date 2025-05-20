package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.common.domain.entity.OperLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {

    /*
     * 添加操作日志
     * @param operLog
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    void insertOperLog(OperLog operLog);

    /*
     * 删除操作日志
     * @param operId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void deleteOperLog(long operId);

    /*
     * 分页获取操作日志列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.common.domain.entity.OperLog>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<OperLog> listOperLogByPage(Page<OperLog> page, @Param("paramMap")Map<String, Object> paramMap);

}