package com.ren.common.core.domain.vo;

import cn.hutool.system.JvmInfo;
import cn.hutool.system.JvmSpecInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.lang.management.ManagementFactory;

/**
 * Jvm相关信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@Accessors(chain = true) // 开启链式调用
@AllArgsConstructor
@NoArgsConstructor
public class JvmInfoVO
{
	/**JVM 实现名称 (如 "OpenJDK 64-Bit Server VM")*/
	private String jvmName;
	/**JVM 实现版本号*/
	private String jvmVersion;
	/**JVM 供应商信息*/
	private String jvmVendor;
	/**JVM 附加信息 (运行模式等)*/
	private String jvmInfoStr;
	/**JVM 规范名称*/
	private String jvmSpecName;
	/**JVM 规范版本*/
	private String jvmSpecVersion;
	/**JVM 规范维护者*/
	private String jvmSpecVendor;
	/**JVM运行参数*/
	private String inputArgs;

	public JvmInfoVO(JvmInfo jvmInfo, JvmSpecInfo jvmSpecInfo) {
		jvmName = jvmInfo.getName();
		jvmVersion = jvmInfo.getVersion();
		jvmVendor = jvmInfo.getVendor();
		jvmInfoStr = jvmInfo.getInfo();

		jvmSpecName = jvmSpecInfo.getName();
		jvmSpecVersion = jvmSpecInfo.getVersion();
		jvmSpecVendor = jvmSpecInfo.getVendor();

		inputArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
	}
}