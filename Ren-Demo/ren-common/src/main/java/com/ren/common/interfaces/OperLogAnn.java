package com.ren.common.interfaces;

import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.enums.OperatorType;

import java.lang.annotation.*;

/*
 * 用在需要生成注解的方法上
 * @author ren
 * @date 2025/05/20 15:24
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLogAnn {

	/**
	 * 模块
	 */
	public String title() default "";

	/**
	 * 功能
	 */
	public BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 操作人类别
	 */
	public OperatorType operatorType() default OperatorType.MANAGE;

	/**
	 * 是否保存请求的参数
	 */
	public boolean isSaveRequestData() default true;

	/**
	 * 是否保存响应的参数
	 */
	public boolean isSaveResponseData() default true;

	/**
	 * 排除指定的请求参数
	 */
	public String[] excludeParamNames() default {};

}