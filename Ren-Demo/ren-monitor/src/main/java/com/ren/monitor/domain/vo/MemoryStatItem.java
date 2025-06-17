
package com.ren.monitor.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryStatItem {

	/**最小值*/
	Double min;
	/**最大值*/
	Double max;
	/**当前值*/
	Double value;

}
