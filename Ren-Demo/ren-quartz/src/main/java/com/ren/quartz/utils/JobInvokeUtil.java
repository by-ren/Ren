package com.ren.quartz.utils;
import cn.hutool.core.util.ObjUtil;
import com.ren.common.manager.SpringManager;
import com.ren.common.utils.StringUtils;
import com.ren.quartz.domain.entity.TimedTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务执行工具类：通过反射机制动态调用定时任务方法
 *
 * 主要功能：
 *  1. 解析任务配置中的目标调用字符串
 *  2. 根据配置动态获取Bean实例（Spring容器或直接实例化）
 *  3. 反射执行目标方法（支持多参数类型）
 *
 * @author admin
 */
public class JobInvokeUtil
{
	/**
	 * 执行定时任务的目标方法
	 *
	 * @param timedTask 定时任务对象，包含调用目标信息
	 * @throws Exception 可能抛出的所有异常（反射异常、类加载异常等）
	 */
	public static void invokeMethod(TimedTask timedTask) throws Exception
	{
		//获取调用目标字符串
		String invokeTarget = timedTask.getInvokeTarget();
		//获取目标字符串中的Bean名称
		String beanName = getBeanName(invokeTarget);
		//获取目标字符串中的方法名称
		String methodName = getMethodName(invokeTarget);
		//获取方法参数
		List<Object[]> methodParams = getMethodParams(invokeTarget);

		// 判断目标类型并获取Bean实例
		Object bean = null;
		if (!isValidClassName(beanName))
		{
			// 如果是SpringBean则从Spring容器获取Bean
			bean = SpringManager.getBean(beanName);
		}
		else
		{
			// 如果是完整类名，则直接实例化类对象
			bean = Class.forName(beanName).getDeclaredConstructor().newInstance();
		}
		invokeMethod(bean, methodName, methodParams);
	}

	/**
	 * 从调用目标字符串解析Bean名称/类全限定名
	 * 格式示例：beanName.methodName(param) 或 com.xyxt.Service.method(param)
	 *
	 * @param invokeTarget 调用目标字符串
	 * @return bean名称或类全限定名
	 */
	public static String getBeanName(String invokeTarget)
	{
		// 截取"("前的部分（方法名和参数前的完整声明）
		// StringUtils.subBefore作用是返回 目标字符 第一次出现前的子串（最后一个参数表示是第一次出现前的字符还是最后一次出现前的字符，false表示第一次出现前，true表示最后一次出现前）
		// StringUtils.subBefore还有一个四个参数的方法，多的那个参数表示是否包含分隔符
		String beanName = StringUtils.subBefore(invokeTarget, "(", false);
		// 取最后一个点之前的内容（也就是Bean名）
		return StringUtils.subBefore(beanName, ".", true);
	}

	/**
	 * 从调用目标字符串解析方法名
	 *
	 * @param invokeTarget 调用目标字符串
	 * @return 方法名称
	 */
	public static String getMethodName(String invokeTarget)
	{
		//获取"("前的部分（方法名和参数前的完整声明）
		String methodName = StringUtils.subBefore(invokeTarget, "(", false);
		// 取最后一个点之后的内容（也就是方法名）
		return StringUtils.subAfter(methodName, ".", true);
	}

	/**
	 * 解析方法参数列表（支持多类型参数解析）
	 *
	 * 参数格式处理规则：
	 *  1. 单引号/双引号包裹的字符串 -> String类型
	 *  2. "true"/"false"（不区分大小写）-> Boolean类型
	 *  3. 以'L'结尾的数值 -> Long类型
	 *  4. 以'D'结尾的数值 -> Double类型
	 *  5. 其他数值 -> Integer类型
	 *
	 * @param invokeTarget 调用目标字符串
	 * @return 参数列表集合，每个元素为[参数值, 参数类型]数组
	 */
	public static List<Object[]> getMethodParams(String invokeTarget)
	{
		// 提取括号内的参数字符串
		// StringUtils.subBetween作用是返回 open 和 close 之间的子符串
		String methodStr = StringUtils.subBetween(invokeTarget, "(", ")");
		if (StringUtils.isEmpty(methodStr))
		{
			return null;
		}

		// 使用正则分割参数（避免分割被引号包裹的逗号）
		String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
		List<Object[]> paramsList = new LinkedList<>();

		for (String param : methodParams)
		{
			// StringUtils.trimToEmpty去除首尾空白，null 返回空字符串
			param = StringUtils.trimToEmpty(param);
			// 字符串类型识别（以单引号或者双引号开头）
			// StringUtils.startWithAny作用是检查字符串是否以指定字符开头，参数是一个可变参数，可以传多个，满足一个就返回true
			if (StringUtils.startWithAny(param, "'", "\""))
			{
				paramsList.add(new Object[] {
						// StringUtils.sub 作用是截取 start 到 end-1 位置的子串
						// 去掉字符串中的引号，仅保留参数值，之后将其转换为字符串类型
						StringUtils.sub(param, 1, param.length() - 1),
						String.class
				});
			}
			// 布尔类型识别
			else if ("true".equalsIgnoreCase(param) || "false".equalsIgnoreCase(param))
			{
				paramsList.add(new Object[] {
						Boolean.valueOf(param),
						Boolean.class
				});
			}
			// 长整型识别（后缀L）
			else if (com.baomidou.mybatisplus.core.toolkit.StringUtils.endsWith(param, "L"))
			{
				paramsList.add(new Object[] {
						Long.valueOf(StringUtils.sub(param, 0, param.length() - 1)),
						Long.class
				});
			}
			// 双精度浮点型识别（后缀D）
			else if (com.baomidou.mybatisplus.core.toolkit.StringUtils.endsWith(param, "D"))
			{
				paramsList.add(new Object[] {
						Double.valueOf(StringUtils.sub(param, 0, param.length() - 1)),
						Double.class
				});
			}
			// 默认按整型处理
			else
			{
				paramsList.add(new Object[] {
						Integer.valueOf(param),
						Integer.class
				});
			}
		}
		return paramsList;
	}

	/**
	 * 校验字符串是否为完整类名（通过点符号数量判断）
	 *
	 * @param str 待校验字符串
	 * @return true: 是完整的类名 (如com.example.Service), false: 是Spring Bean名 (如taskService)
	 */
	public static boolean isValidClassName(String str)
	{
		// StringUtils.count作用是统计目标字符在原字符串中出现的次数
		return StringUtils.count(str, ".") > 1;
	}

	/**
	 * 反射执行目标方法（核心执行逻辑）
	 *
	 * @param bean 目标对象实例
	 * @param methodName 待执行方法名
	 * @param methodParams 方法参数列表（值和类型）
	 * @throws NoSuchMethodException 方法不存在异常
	 * @throws IllegalAccessException 方法访问权限异常
	 * @throws InvocationTargetException 方法执行异常
	 */
	private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		// 根据参数情况进行有参/无参调用
		if (ObjUtil.isNotNull(methodParams) && methodParams.size() > 0)
		{
			// 获取参数类型数组
			Class<?>[] paramTypes = getMethodParamsType(methodParams);
			// 根据方法名和参数类型数组精确匹配到对应方法（防止方法重载（方法名相同，参数不同））
			Method method = bean.getClass().getMethod(methodName, paramTypes);
			// 获取到相应的参数列表，执行方法
			method.invoke(bean, getMethodParamsValue(methodParams));
		}
		else
		{
			// 执行无参方法
			Method method = bean.getClass().getMethod(methodName);
			method.invoke(bean);
		}
	}

	/**
	 * 提取方法参数类型数组
	 *
	 * @param methodParams 参数列表集合
	 * @return Class数组，包含所有参数的类型
	 */
	public static Class<?>[] getMethodParamsType(List<Object[]> methodParams)
	{
		Class<?>[] paramTypes = new Class<?>[methodParams.size()];
		for (int i = 0; i < methodParams.size(); i++)
		{
			paramTypes[i] = (Class<?>) methodParams.get(i)[1];
		}
		return paramTypes;
	}

	/**
	 * 提取方法参数值数组
	 *
	 * @param methodParams 参数列表集合
	 * @return Object数组，包含所有参数的值
	 */
	public static Object[] getMethodParamsValue(List<Object[]> methodParams)
	{
		Object[] paramValues = new Object[methodParams.size()];
		for (int i = 0; i < methodParams.size(); i++)
		{
			paramValues[i] = methodParams.get(i)[0];
		}
		return paramValues;
	}
}