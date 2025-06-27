package com.ren.localstorage.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.localstorage.entity.ImageLog;

@Mapper
public interface ImageLogMapper extends BaseMapper<ImageLog> {

    /**
     * 添加图片日志
     * 
     * @param imageLog
     * @author ren
     * @date 2025/06/19 19:22
     */
    void insertImageLog(ImageLog imageLog);

}