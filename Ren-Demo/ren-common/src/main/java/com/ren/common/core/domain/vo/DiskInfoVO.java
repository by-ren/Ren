package com.ren.common.core.domain.vo;

import cn.hutool.core.util.NumberUtil;
import com.ren.common.utils.BigDecimalUtils;
import com.ren.common.utils.file.FileUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import oshi.software.os.OSFileStore;

/**
 * 磁盘相关信息
 * @author ren
 * @date 2025/06/07 17:19
 */
@Data
@Accessors(chain = true) // 开启链式调用
public class DiskInfoVO {

	/**
	 * 盘符路径
	 */
	private String dirName;

	/**
	 * 盘符类型
	 */
	private String sysTypeName;

	/**
	 * 文件类型
	 */
	private String typeName;

	/**
	 * 总大小
	 */
	private String total;

	/**
	 * 剩余大小
	 */
	private String free;

	/**
	 * 已经使用量
	 */
	private String used;

	/**
	 * 资源的使用率
	 */
	private String usage;

	public DiskInfoVO(OSFileStore fs){
		dirName = fs.getMount();
		sysTypeName = fs.getType();
		typeName = fs.getName();
		total = FileUtils.convertFileSize(fs.getTotalSpace());
		free = FileUtils.convertFileSize(fs.getUsableSpace());
		used = FileUtils.convertFileSize(fs.getTotalSpace() - fs.getUsableSpace());
		usage = NumberUtil.decimalFormat("0.00%", BigDecimalUtils.toDouble(BigDecimalUtils.divide((fs.getTotalSpace() - fs.getUsableSpace()), fs.getTotalSpace(), 2)));
	}

}