package com.ren.common.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 异常处理工具类（基于Hutool实现）
 */
public class ExceptionUtils {

	/**
	 * 获取异常的完整堆栈跟踪信息
	 *
	 * @param e 异常对象(Throwable或其子类)
	 * @return 包含完整堆栈跟踪信息的字符串
	 *
	 * @implNote 使用Hutool的stacktraceToString方法
	 */
	public static String getExceptionMessage(Throwable e) {
		// Hutool方法会自动处理null和异常链
		return ExceptionUtil.stacktraceToString(e);
	}

	/**
	 * 获取异常根源错误消息（优化版）
	 *
	 * @param e 异常对象
	 * @return 异常根源消息的字符串
	 *
	 * @implNote
	 * 1. 使用Hutool的getRootCauseMessage获取根本原因
	 * 2. 增强空值处理和结果优化
	 */
	public static String getRootErrorMessage(Throwable e) {
		// Hutool方法自动查找根因并获取消息
		String rootMsg = ExceptionUtil.getRootCauseMessage(e);

		// 优化结果格式（去掉类名前缀）
		if (StringUtils.isNotBlank(rootMsg) && rootMsg.contains(":")) {
			// 示例: "java.lang.NullPointerException: 参数不能为空" → "参数不能为空"
			return rootMsg.substring(rootMsg.indexOf(':') + 1).trim();
		}
		return StringUtils.blankToDefault(rootMsg, "未知错误");
	}

	/**
	 * 获取精简版异常信息（生产环境推荐）
	 *
	 * @param e 异常对象
	 * @return 包含类名和首行信息的简短描述
	 */
	public static String getBriefErrorMessage(Throwable e) {
		Throwable root = ExceptionUtil.getRootCause(e);
		root = root != null ? root : e;

		return StringUtils.format("{}: {}",
				root.getClass().getSimpleName(),
				StringUtils.blankToDefault(root.getMessage(), "无错误信息"));
	}

	/**
	 * 诊断异常是否为严重系统错误
	 *
	 * @param e 异常对象
	 * @return true表示需要立即告警的错误类型
	 */
	public static boolean isCriticalError(Throwable e) {
		Throwable root = ExceptionUtil.getRootCause(e);
		Class<?> exType = root != null ? root.getClass() : e.getClass();

		// 识别需要立即告警的异常类型
		return exType == OutOfMemoryError.class ||
				exType == StackOverflowError.class ||
				exType == ThreadDeath.class;
	}
}