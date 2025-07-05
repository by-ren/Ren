package com.ren.common.core.exception;

/**
 * 自定义异常类
 * @author ren
 * @date 2025/05/08 11:56
 */
public class BusinessException extends RuntimeException {
	private final int code;

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}