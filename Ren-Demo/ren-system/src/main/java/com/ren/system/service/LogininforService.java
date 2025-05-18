package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.Config;

import java.util.Map;

public interface ConfigService extends IService<Config> {

    /*
     * 添加配置
     * @param config
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    long addConfig(Config config,String createBy);

    /*
     * 删除配置
     * @param configId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void removeConfig(long configId);

    /*
     * 编辑配置
     * @param config
     * @author admin
     * @date 2025/05/18 13:49
     */
    void modifyConfig(Config config);

    /*
     * 分页获取配置列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Config>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<Config> listConfigByPage(Map<String,Object> paramMap);

    /*
     * 获取配置详情
     * @param configId
     * @return com.ren.system.entity.Config
     * @author admin
     * @date 2025/05/18 13:50
     */
    Config getConfigById(long configId);
}