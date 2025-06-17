package com.ren.monitor.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicInfoItem {

	/**标题*/
	String label;
	/**内容*/
	String value;

}
