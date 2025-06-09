
package com.ren.common.domain.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 项目相关信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@Accessors(chain = true) // 开启链式调用
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoBO
{
	// 获取项目路径
	private String projectDir;

	// 项目启动时间
	private String startTimeStr;

	// 项目运行时长
	private String runningTimeStr;

}