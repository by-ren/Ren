package com.ren.framework.handler;

import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.constant.HttpStatus;
import com.ren.common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/*
 * 全局异常处理器
 * @author ren
 * @date 2025/05/08 11:56
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 处理参数校验异常（JSR 303规范）
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public AjaxResult handleValidationException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		// 拼接错误信息（示例：name不能为空, age必须大于0）
		String errorMsg = fieldErrors.stream()
				.map(error -> error.getField() + error.getDefaultMessage())
				.collect(Collectors.joining(", "));

		log.error("参数校验失败: {}", errorMsg);
		return AjaxResult.error(HttpStatus.BAD_REQUEST, "参数错误: " + errorMsg);
	}

	/**
	 * 处理自定义业务异常（需自行定义 BusinessException）
	 */
	@ExceptionHandler(BusinessException.class)
	public AjaxResult handleBusinessException(BusinessException ex) {
		log.error("业务异常[code={}]: {}", ex.getCode(), ex.getMessage());
		return AjaxResult.error(ex.getCode(), ex.getMessage());
	}

	/**
	 * 处理其他未捕获异常
	 */
	@ExceptionHandler(Exception.class)
	public AjaxResult handleGlobalException(Exception ex) {
		log.error("系统异常: ", ex); // 打印堆栈信息
		return AjaxResult.error(
				HttpStatus.ERROR,
				"系统繁忙，请稍后再试"
		);
	}
}