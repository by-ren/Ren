package com.ren.admin.aop;

import cn.hutool.core.util.ObjUtil;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.utils.FastJSON2Utils;
import com.ren.common.utils.ip.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// 该切面用于给访问接口添加操作日志
@Aspect
@Component
@Slf4j
public class OperLogAspect {

	/*
	 * 匹配所有Controller的public方法
	 * @author ren
	 * @date 2025/05/20 13:24
	 */
	@Pointcut("execution(public * com.ren.admin.controller..*.*(..))")
	public void controllerPointcut() {}


	/*
	 * 拦截所有符合controllerPointcut的请求
	 * @param joinPoint
	 * @return java.lang.Object
	 * @author ren
	 * @date 2025/05/20 13:24
	 */
	@Around("controllerPointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		// 非 Web 请求直接放行
		if (attributes == null) {
			return joinPoint.proceed();
		}

		//获取请求中的详情
		HttpServletRequest request = attributes.getRequest();
		// 获取方法签名信息
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		// 获取请求参数（过滤敏感字段之后的内容）
		Object[] args = joinPoint.getArgs();
		//过滤掉请求参数中的敏感字段
		String params = FastJSON2Utils.filterSensitiveFields(FastJSON2Utils.EXCLUDE_PROPERTIES,args);
		//添加请求进入的日志
		log.info(">>>> 请求进入 - URL: {}, RequestMethod: {}, IP: {}, Class: {}, Method: {}, Param: {}",
				request.getRequestURL(),
				request.getMethod(),
				IpUtils.getIpAddr(),
				signature.getDeclaringTypeName(),
				signature.getName(),
				params);

		//请求是否成功
		boolean isSuccess = false;
		//获取请求结果相关
		Object result = null;
		//获取请求耗时
		long startTime = System.currentTimeMillis();
		try {
			// 执行目标方法
			result = joinPoint.proceed();
			// 如果请求失败会直接报错
			isSuccess = true;
		}catch (Exception e){
			log.error(">>>> 请求失败" ,e);
		}
		long endTime = System.currentTimeMillis();
		if(isSuccess){
			log.info(">>>> 请求成功，耗时: {}ms", endTime - startTime);
		}

		//返回执行结果
		return result;
	}

}