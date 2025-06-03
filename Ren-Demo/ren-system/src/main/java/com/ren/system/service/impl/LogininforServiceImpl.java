package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.utils.PageUtils;
import com.ren.common.domain.entity.Logininfor;
import com.ren.system.mapper.LogininforMapper;
import com.ren.system.service.LogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogininforServiceImpl extends ServiceImpl<LogininforMapper, Logininfor> implements LogininforService {

    @Autowired
    private LogininforMapper logininforMapper;

    /**
     * 添加登录日志
     * @param logininfor
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public long addLogininfor(Logininfor logininfor) {
        logininforMapper.insertLogininfor(logininfor);
        return logininfor.getInfoId();
    }

    /**
     * 删除登录日志
     * @param infoId
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public void removeLogininfor(long infoId) {
        logininforMapper.deleteLogininfor(infoId);
    }

    /**
     * 分页获取登录日志列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.common.domain.entity.Logininfor>
     * @author ren
     * @date 2025/05/18 13:50
     */
    @Override
    public IPage<Logininfor> listLogininforByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<Logininfor> logininforList = logininforMapper.listLogininforByPage(PageUtils.createPage(Logininfor.class),paramMap);
        return logininforList;
    }

    /**
     * 获取登录日志详情
     * @param infoId
     * @return com.ren.common.domain.entity.Logininfor
     * @author ren
     * @date 2025/05/18 13:50
     */
    @Override
    public Logininfor getLogininforById(long infoId) {
        Logininfor logininfor = logininforMapper.selectById(infoId);
        return logininfor;
    }
}