package com.ren.framework.aop;

import com.ren.common.domain.interfaces.Pageable;
import com.ren.common.utils.PageUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect  // 声明为切面
@Component
public class PageAspect {

	// 拦截所有标记了 @Pageable 的方法
	@Around("@annotation(pageable)")
	public Object around(ProceedingJoinPoint joinPoint, Pageable pageable) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return joinPoint.proceed(); // 非 Web 请求直接放行
		}
		try {
			PageUtils.setThreadLocalPage();
			// 执行目标方法
			return joinPoint.proceed();
		} finally {
			// 清理 ThreadLocal
			PageUtils.cleanThreadLocalPage();
		}
	}
}