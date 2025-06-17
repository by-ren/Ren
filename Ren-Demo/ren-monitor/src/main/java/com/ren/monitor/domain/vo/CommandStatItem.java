package com.ren.monitor.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommandStatItem {

	/**命令名称*/
	String name;
	/**命令执行数量*/
	Integer value;

}
