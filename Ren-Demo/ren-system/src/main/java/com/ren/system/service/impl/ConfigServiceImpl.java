package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.PageUtils;
import com.ren.common.utils.StringUtils;
import com.ren.system.entity.Config;
import com.ren.system.mapper.ConfigMapper;
import com.ren.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 添加配置
     * @param config
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public long addConfig(Config config,String createBy) {
        config.setCreateBy(createBy);
        config.setCreateTime(DateUtils.currentSeconds());
        configMapper.insertConfig(config);
        return config.getConfigId();
    }

    /**
     * 删除配置
     * @param configId
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public void removeConfig(long configId) {
        configMapper.deleteConfig(configId);
    }

    /**
     * 编辑配置
     * @param config
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public void modifyConfig(Config config,String updateBy) {
        config.setUpdateBy(updateBy);
        config.setUpdateTime(DateUtils.currentSeconds());
        configMapper.updateConfig(config);
    }

    /**
     * 分页获取配置列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Config>
     * @author ren
     * @date 2025/05/18 13:50
     */
    @Override
    public IPage<Config> listConfigByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StringUtils.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StringUtils.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<Config> configList = configMapper.listConfigByPage(PageUtils.createPage(Config.class),paramMap);
        return configList;
    }

    /**
     * 获取配置详情
     * @param configId
     * @return com.ren.system.entity.Config
     * @author ren
     * @date 2025/05/18 13:50
     */
    @Override
    public Config getConfigById(long configId) {
        Config config = configMapper.selectById(configId);
        return config;
    }

    /**
     * 获取配置详情
     * @param configKey
     * @return com.ren.system.entity.Config
     * @author ren
     * @date 2025/05/23 13:12
     */
    @Override
    public Config getConfigByConfigKey(String configKey) {
        Config config = configMapper.selectConfigByConfigKey(configKey);
        return config;
    }
}