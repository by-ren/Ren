package com.ren.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.cloudstorage.domain.entity.CloudImageLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CloudImageLogMapper extends BaseMapper<CloudImageLog> {

	/**
	 * 添加图片日志
	 * @param cloudImageLog
	 * @author ren
	 * @date 2025/06/19 19:22
	 */
	void insertImageLog(CloudImageLog cloudImageLog);

}