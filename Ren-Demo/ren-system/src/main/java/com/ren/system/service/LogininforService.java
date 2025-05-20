package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.domain.entity.Logininfor;

import java.util.Map;

public interface LogininforService extends IService<Logininfor> {

    /*
     * 添加登录日志
     * @param logininfor
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    long addLogininfor(Logininfor logininfor);

    /*
     * 删除登录日志
     * @param infoId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void removeLogininfor(long infoId);

    /*
     * 分页获取登录日志列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.common.domain.entity.Logininfor>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<Logininfor> listLogininforByPage(Map<String,Object> paramMap);

    /*
     * 获取登录日志详情
     * @param infoId
     * @return com.ren.common.domain.entity.Logininfor
     * @author admin
     * @date 2025/05/18 13:50
     */
    Logininfor getLogininforById(long infoId);
}