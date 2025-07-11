package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.system.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

    /**
     * 添加配置
     * @param config
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    void insertConfig(Config config);

    /**
     * 删除配置
     * @param configId
     * @author ren
     * @date 2025/05/18 13:49
     */
    void deleteConfig(long configId);

    /**
     * 编辑配置
     * @param config
     * @author ren
     * @date 2025/05/18 13:49
     */
    void updateConfig(Config config);

    /**
     * 分页获取配置列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Config>
     * @author ren
     * @date 2025/05/18 13:50
     */
    IPage<Config> listConfigByPage(Page<Config> page, @Param("paramMap")Map<String, Object> paramMap);

    /**
     * 获取配置详情
     * @param configKey
     * @return com.ren.system.entity.Config
     * @author ren
     * @date 2025/05/23 13:12
     */
	Config selectConfigByConfigKey(String configKey);
}