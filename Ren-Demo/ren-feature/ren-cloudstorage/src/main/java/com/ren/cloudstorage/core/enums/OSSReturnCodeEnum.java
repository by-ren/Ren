package com.ren.cloudstorage.core.enums;

public enum OSSReturnCodeEnum
{
	UPLOAD_ERROR("OSS_001", "文件上传失败"),
	PREVIEW_GENERATION_ERROR("OSS_002", "预览链接生成失败"),
	CONFIGURATION_ERROR("OSS_003", "配置信息错误");

	private final String code;       // 错误码
	private final String message;    // 错误描述

	OSSReturnCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
