


package com.ren.common.domain.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;
import oshi.hardware.NetworkIF;

/**
 * 网络相关信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@Accessors(chain = true) // 开启链式调用
public class NetWorkInfoBO
{
	/**名称*/
	private String name;
	/**显示名称*/
	private String displayName;
	/**MAC 地址*/
	private String macaddr;
	/**IPv4*/
	private String iPv4addr;
	/**IPv6*/
	private String iPv6addr;
	/**已发送字节数*/
	private long bytesSent;
	/**已接收字节数*/
	private long bytesRecv;
	/**已发送数据包数*/
	private long packetsSent;
	/**已接收数据包数*/
	private long packetsRecv;

	public NetWorkInfoBO(NetworkIF networkIF){
		name = networkIF.getName();
		displayName = networkIF.getDisplayName();
		macaddr = networkIF.getMacaddr();
		//同一个接口也可能存在多个IPV4地址
		iPv4addr = String.join(", ", networkIF.getIPv4addr());
		//同一个接口也可能存在多个IPV6地址
		iPv6addr = String.join(", ", networkIF.getIPv6addr());
		bytesSent = networkIF.getBytesSent();
		bytesRecv = networkIF.getBytesRecv();
		packetsSent = networkIF.getPacketsSent();
		packetsRecv = networkIF.getPacketsRecv();
	}
}