package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.utils.PageUtils;
import com.ren.system.entity.OperLog;
import com.ren.system.mapper.OperLogMapper;
import com.ren.system.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

    @Autowired
    private OperLogMapper operLogMapper;

    /*
     * 添加操作日志
     * @param operLog
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public long addOperLog(OperLog operLog) {
        operLogMapper.insertOperLog(operLog);
        return operLog.getOperId();
    }

    /*
     * 删除操作日志
     * @param operId
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public void removeOperLog(long operId) {
        operLogMapper.deleteOperLog(operId);
    }

    /*
     * 分页获取操作日志列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.OperLog>
     * @author admin
     * @date 2025/05/18 13:50
     */
    @Override
    public IPage<OperLog> listOperLogByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<OperLog> operLogList = operLogMapper.listOperLogByPage(PageUtils.createPage(OperLog.class),paramMap);
        return operLogList;
    }

    /*
     * 获取操作日志详情
     * @param operId
     * @return com.ren.system.entity.OperLog
     * @author admin
     * @date 2025/05/18 13:50
     */
    @Override
    public OperLog getOperLogById(long operId) {
        OperLog operLog = operLogMapper.selectById(operId);
        return operLog;
    }
}