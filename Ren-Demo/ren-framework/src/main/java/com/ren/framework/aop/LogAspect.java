package com.ren.framework.aop;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.ren.common.constant.AppConstants;
import com.ren.common.domain.bo.LoginUser;
import com.ren.common.domain.entity.OperLog;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.utils.FastJSON2Utils;
import com.ren.common.utils.ip.IpUtils;
import com.ren.common.utils.SecurityUtils;
import com.ren.common.utils.ServletUtils;
import com.ren.framework.manager.AsyncManager;
import com.ren.framework.manager.factory.AsyncFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

//该切面用于添加加了OperLogAnn注解的方法，主要用于添加操作日志
@Aspect
@Component
@Slf4j
public class LogAspect {


	/** 排除敏感属性字段 */
	public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

	/** 计算操作消耗时间 */
	private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

	/**
	 * 处理请求前执行
	 */
	@Before(value = "@annotation(controllerLog)")
	public void boBefore(JoinPoint joinPoint, OperLogAnn controllerLog)
	{
		TIME_THREADLOCAL.set(System.currentTimeMillis());
	}

	/**
	 * 处理完请求后执行
	 *
	 * @param joinPoint 切点
	 */
	@AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, OperLogAnn controllerLog, Object jsonResult)
	{
		handleLog(joinPoint, controllerLog, null, jsonResult);
	}

	/**
	 * 拦截异常操作
	 *
	 * @param joinPoint 切点
	 * @param e 异常
	 */
	@AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, OperLogAnn controllerLog, Exception e)
	{
		handleLog(joinPoint, controllerLog, e, null);
	}

	protected void handleLog(final JoinPoint joinPoint, OperLogAnn controllerLog, final Exception e, Object jsonResult)
	{
		try
		{
			// 获取当前的用户
			LoginUser loginUser = SecurityUtils.getLoginUser();

			// *========数据库日志=========*//
			OperLog operLog = new OperLog();
			operLog.setIsNormal(AppConstants.COMMON_BYTE_YES);
			// 请求的地址
			String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
			operLog.setOperIp(ip);
			operLog.setOperUrl(StrUtil.sub(ServletUtils.getRequest().getRequestURI(), 0, 255));
			if (loginUser != null)
			{
				operLog.setOperName(loginUser.getUsername());
			}

			if (e != null)
			{
				operLog.setIsNormal(AppConstants.COMMON_BYTE_NO);
				operLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
			}
			// 设置方法名称
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			operLog.setMethod(className + "." + methodName + "()");
			// 设置请求方式
			operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
			// 处理设置注解上的参数
			getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
			// 设置消耗时间
			operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
			// 异步添加操作日志
			AsyncManager.me().execute(AsyncFactory.addOperLog(operLog));
		}
		catch (Exception exp)
		{
			// 记录本地异常日志
			log.error("异常信息:{}", exp.getMessage());
			exp.printStackTrace();
		}
		finally
		{
			TIME_THREADLOCAL.remove();
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 *
	 * @param log 日志
	 * @param operLog 操作日志
	 * @throws Exception
	 */
	public void getControllerMethodDescription(JoinPoint joinPoint, OperLogAnn log, OperLog operLog, Object jsonResult) throws Exception
	{
		// 设置action动作(ordinal方法返回的是对象在枚举类中的序号)
		operLog.setBusinessType(Convert.intToByte(log.businessType().ordinal()));
		// 设置标题
		operLog.setTitle(log.title());
		// 设置操作人类别(ordinal方法返回的是对象在枚举类中的序号)
		operLog.setOperatorType(Convert.intToByte(log.operatorType().ordinal()));
		// 是否需要保存request，参数和值
		if (log.isSaveRequestData())
		{
			// 获取参数的信息，传入到数据库中。
			setRequestValue(joinPoint, operLog, log.excludeParamNames());
		}
		// 是否需要保存response，参数和值
		if (log.isSaveResponseData() && ObjUtil.isNotNull(jsonResult))
		{
			operLog.setJsonResult(StrUtil.sub(JSON.toJSONString(jsonResult), 0, 2000));
		}
	}

	/**
	 * 获取请求的参数，放到log中
	 *
	 * @param operLog 操作日志
	 * @throws Exception 异常
	 */
	private void setRequestValue(JoinPoint joinPoint, OperLog operLog, String[] excludeParamNames) throws Exception
	{
		//获取请求链接中的请求参数（GET请求，DELETE请求）
		Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
		//请求方式（GET POST PUT DELETE）
		String requestMethod = operLog.getRequestMethod();
		//如果请求链接中的请求参数为空，并且请求方式为POST或PUT，则获取请求体中的参数
		if (ObjUtil.isEmpty(paramsMap) && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))){
			operLog.setOperParam(StrUtil.sub(FastJSON2Utils.filterSensitiveFields(excludeParamNames,joinPoint.getArgs()), 0, 2000));
		}else{
			operLog.setOperParam(StrUtil.sub(FastJSON2Utils.filterSensitiveFields(excludeParamNames,paramsMap), 0, 2000));
		}
	}

	/**
	 * 判断是否需要过滤的对象。
	 *
	 * @param o 对象信息。
	 * @return 如果是需要过滤的对象，则返回true；否则返回false。
	 */
	@SuppressWarnings("rawtypes")
	public boolean isFilterObject(final Object o)
	{
		Class<?> clazz = o.getClass();
		if (clazz.isArray())
		{
			return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
		}
		else if (Collection.class.isAssignableFrom(clazz))
		{
			Collection collection = (Collection) o;
			for (Object value : collection)
			{
				return value instanceof MultipartFile;
			}
		}
		else if (Map.class.isAssignableFrom(clazz))
		{
			Map map = (Map) o;
			for (Object value : map.entrySet())
			{
				Map.Entry entry = (Map.Entry) value;
				return entry.getValue() instanceof MultipartFile;
			}
		}
		return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
				|| o instanceof BindingResult;
	}
}