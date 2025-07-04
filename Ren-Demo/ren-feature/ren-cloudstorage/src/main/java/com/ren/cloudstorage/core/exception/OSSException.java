package com.ren.cloudstorage.core.exception;

import com.ren.cloudstorage.core.enums.OSSReturnCodeEnum;

/**
 * 云存储操作自定义异常
 */
public class OSSException extends RuntimeException {
  private final String code;  // 错误码

  /**
   * 创建带错误码和消息的异常
   * @param code 错误码
   * @param message 错误描述
   */
  public OSSException(String code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * 创建带错误码和原始异常的异常
   * @param code 错误码
   * @param cause 原始异常
   */
  public OSSException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  /**
   * 创建带错误码和消息和原始异常的异常
   * @param ossReturnCodeEnum
   * @param cause
   * @author ren
   * @date 2025/06/19 19:26
   */
  public OSSException(OSSReturnCodeEnum ossReturnCodeEnum, Throwable cause) {
    super(ossReturnCodeEnum.getMessage(),cause);
    this.code = ossReturnCodeEnum.getCode();
  }

  public String getCode() {
    return code;
  }
}