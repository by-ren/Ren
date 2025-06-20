package com.ren.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.cloudstorage.domain.entity.ImageLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageLogMapper extends BaseMapper<ImageLog> {

	/**
	 * 添加图片日志
	 * @param imageLog
	 * @author ren
	 * @date 2025/06/19 19:22
	 */
	void insertImageLog(ImageLog imageLog);

}