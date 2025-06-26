package com.ren.common.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean 工具类（扩展Spring的BeanUtils）
 *
 * @author ren
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

	/** Bean方法名中属性名开始的下标（get/set后的第一个字符位置） */
	private static final int BEAN_METHOD_PROP_INDEX = 3;

	/** 匹配getter方法的正则表达式 */
	private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

	/** 匹配setter方法的正则表达式 */
	private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

	/**
	 * Bean属性复制工具方法（增强异常处理）
	 * 方法特征：不需要两个对象是同一种类型！这个方法执行的是基于属性名的复制而非基于类型匹配，只要两个对象具有相同名称的属性且类型兼容就可以进行赋值。所以更安全
	 * @param src  源对象（属性来源）
	 * @param dest 目标对象（属性将被覆盖）
	 * @implNote 内部使用Spring的copyProperties方法，捕获异常并打印堆栈
	 */
	public static void copyBeanProp(Object src, Object dest) {
		try {
			// 调用父类的属性复制方法
			copyProperties(src, dest);
		} catch (Exception e) {
			e.printStackTrace();  // 异常处理（实际生产可考虑日志记录）
		}
	}

	/**
	 * 获取对象的所有setter方法
	 *
	 * @param obj 目标对象
	 * @return setter方法列表（无参数且方法名以"set"开头）
	 * @implNote 通过反射获取类中所有set方法（满足：方法名匹配正则、有且仅有一个参数）
	 */
	public static List<Method> getSetterMethods(Object obj) {
		List<Method> setterMethods = new ArrayList<>();
		// 获取所有public方法
		Method[] methods = obj.getClass().getMethods();

		for (Method method : methods) {
			// 创建一个Set方法的匹配器
			Matcher m = SET_PATTERN.matcher(method.getName());
			// 验证：方法名匹配setXxx格式且参数数量为1
			if (m.matches() && (method.getParameterTypes().length == 1)) {
				setterMethods.add(method);
			}
		}
		return setterMethods;
	}

	/**
	 * 获取对象的所有getter方法
	 *
	 * @param obj 目标对象
	 * @return getter方法列表（无参数且方法名以"get"开头）
	 * @implNote 通过反射获取类中所有get方法（满足：方法名匹配正则、无参数）
	 */
	public static List<Method> getGetterMethods(Object obj) {
		List<Method> getterMethods = new ArrayList<>();
		// 获取所有public方法
		Method[] methods = obj.getClass().getMethods();

		for (Method method : methods) {
			// 创建一个Get方法的匹配器
			Matcher m = GET_PATTERN.matcher(method.getName());
			// 验证：方法名匹配getXxx格式且无参数
			if (m.matches() && (method.getParameterTypes().length == 0)) {
				getterMethods.add(method);
			}
		}
		return getterMethods;
	}

	/**
	 * 比较一个Bean的Get/Set方法对属性名是否一致
	 *
	 * @param m1 方法名1（如getName）
	 * @param m2 方法名2（如setName）
	 * @return true-属性名相同，false-属性名不同
	 * @example isMethodPropEquals("getName", "setName") -> true
	 * @implNote 截取方法名中属性部分（去除get/set前缀）进行比较
	 */
	public static boolean isMethodPropEquals(String m1, String m2) {
		// 从第3个字符开始截取（get/set后的属性名部分）
		return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
	}
}