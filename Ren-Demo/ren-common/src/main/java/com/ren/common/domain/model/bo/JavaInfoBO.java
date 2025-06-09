
package com.ren.common.domain.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Java相关信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@Accessors(chain = true) // 开启链式调用
@NoArgsConstructor
@AllArgsConstructor
public class JavaInfoBO
{

	/**Java 完整版本号 (如 "17.0.5")*/
	private String javaVersion;
	/**Java 主版本号整数形式 (如 Java 17 → 17)*/
	private int javaVersionInt;
	/**Java 供应商信息 (如 "Oracle Corporation")*/
	private String javaVendor;
	/**Java 运行时环境名称*/
	private String runtimeName;
	/**Java 运行时环境具体版本*/
	private String runtimeVersion;
	/**JDK/JRE 安装根目录路径*/
	private String javaHome;
	/**Java 平台规范名称*/
	private String javaSpecName;
	/**Java 平台规范版本*/
	private String javaSpecVersion;
	/**Java 规范维护者信息*/
	private String javaSpecVendor;
}