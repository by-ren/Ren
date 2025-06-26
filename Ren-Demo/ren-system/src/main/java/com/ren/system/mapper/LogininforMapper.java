package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.common.domain.entity.Logininfor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface LogininforMapper extends BaseMapper<Logininfor> {

    /**
     * 添加登录日志
     * @param logininfor
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    void insertLogininfor(Logininfor logininfor);

    /**
     * 删除登录日志
     * @param infoId
     * @author ren
     * @date 2025/05/18 13:49
     */
    void deleteLogininfor(long infoId);

    /**
     * 分页获取登录日志列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.common.domain.entity.Logininfor>
     * @author ren
     * @date 2025/05/18 13:50
     */
    IPage<Logininfor> listLogininforByPage(Page<Logininfor> page, @Param("paramMap")Map<String, Object> paramMap);

}