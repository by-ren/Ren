package com.ren.common.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Cpu相关信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // 开启链式调用
public class CpuInfoVO {

	/** CPU核心数 */
	private int cpuNum;
	/** CPU 核心数 */
	private String cpuModel;
	/** CPU 型号 */
	private String used;
	/** CPU 总使用率 (百分比) */
	private String waitUsed;
	/** CPU 等待使用率 (百分比) */
	private String userUsed;
	/** CPU 用户使用率 (百分比) */
	private String sysUsed;
	/** CPU 系统使用率 (百分比) */
	private String free;

}