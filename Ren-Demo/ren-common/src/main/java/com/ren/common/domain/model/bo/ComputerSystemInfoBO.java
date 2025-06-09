

package com.ren.common.domain.model.bo;

import com.ren.common.utils.ip.IpUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import oshi.hardware.ComputerSystem;
import oshi.software.os.OperatingSystem;

/**
 * 电脑系统信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@Accessors(chain = true) // 开启链式调用
@NoArgsConstructor
@AllArgsConstructor
public class ComputerSystemInfoBO
{
	/**制造商*/
	private String manufacturer;
	/**型号*/
	private String model;
	/**序列号*/
	private String serialNumber;

	/**操作系统类型*/
	private String family;
	/**厂商*/
	private String osManufacturer;
	/**版本*/
	private String version;
	/**系统位数*/
	private int bitness;
	/**正常运行时间*/
	private long systemUptime;
	/**运行中进程数*/
	private int processCount;

	/** 服务器名称 */
	private String computerName;
	/** 服务器Ip */
	private String computerIp;
	/** 系统架构 */
	private String osArch;

	public ComputerSystemInfoBO(ComputerSystem computerSystem, OperatingSystem os){
		manufacturer = computerSystem.getManufacturer();
		model = computerSystem.getModel();
		serialNumber = computerSystem.getSerialNumber();
		family = os.getFamily();
		osManufacturer = os.getManufacturer();
		version = os.getVersionInfo().getVersion();
		bitness = os.getBitness();
		systemUptime = os.getSystemUptime();
		processCount = os.getProcessCount();
		computerName = IpUtils.getHostName();
		computerIp = IpUtils.getHostIp();
		osArch = System.getProperties().getProperty("os.arch");
	}
}