package com.ren.common.utils.json;

import com.alibaba.fastjson2.JSONReader.Feature;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

/**
 * FastJSON2 工具增强类
 * @since 2.0.4
 */
public class FastJSON2Utils {

	// 日期时间格式化器
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	// 序列化函数
	private static final Function<LocalDateTime, String> dateTimeSerializer = dateTimeFormatter::format;
	private static final Function<LocalDate, String> dateSerializer = dateFormatter::format;
	private static final Function<LocalTime, String> timeSerializer = timeFormatter::format;
	private static final Function<Date, String> __dateSerializer = json -> dateTimeFormatter.format(json.toInstant());

	// 反序列化函数
	private static final Function<String, LocalDateTime> dateTimeDeserializer = json -> LocalDateTime.parse(json, dateTimeFormatter);
	private static final Function<String, LocalDate> dateDeserializer = json -> LocalDate.parse(json, dateFormatter);
	private static final Function<String, LocalTime> timeDeserializer = json -> LocalTime.parse(json, timeFormatter);
	private static final Function<String, Date> __dateDeserializer = json -> Date.from(LocalDateTime.parse(json, dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant());

	//排除敏感字段
	public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

	// FastJSON2 对象读取器提供者
	private static final ObjectReaderProvider provider;

	static {
		// 初始化类型转换器
		provider = JSONFactory.getDefaultObjectReaderProvider();
		provider.registerTypeConvert(String.class, Date.class, __dateDeserializer);
		provider.registerTypeConvert(String.class, LocalDateTime.class, dateTimeDeserializer);
		provider.registerTypeConvert(String.class, LocalDate.class, dateDeserializer);
		provider.registerTypeConvert(String.class, LocalTime.class, timeDeserializer);
		provider.registerTypeConvert(Date.class, String.class, __dateSerializer);
		provider.registerTypeConvert(LocalDateTime.class, String.class, dateTimeSerializer);
		provider.registerTypeConvert(LocalDate.class, String.class, dateSerializer);
		provider.registerTypeConvert(LocalTime.class, String.class, timeSerializer);
	}

	/**
	 * 创建泛型类型（用于处理List<T>, Map<K,V>等复杂类型）
	 * @param rawType 容器类型（如List.class, Map.class）
	 * @param genericTypes 泛型参数（如String.class, 或Class<?>数组）
	 * @return 构造好的泛型类型对象
	 */
	public static ParameterizedType createGenericType(Type rawType, Type... genericTypes) {
		return new ParameterizedTypeImpl(genericTypes, null, rawType);
	}

	/*===========================================各种类型转换为JSON字符串=================================================*/

	/**
	 * 对象转JSON字符串（基础方法）
	 * @param obj 任意Java对象
	 * @return 对应的JSON字符串，若输入为null则返回null
	 */
	public static String toJson(Object obj) {
		return obj == null ? null : JSON.toJSONString(obj);
	}

	/**
	 * Map对象转JSON字符串
	 * @param map Java Map对象
	 * @return 对应的JSON字符串
	 */
	public static String mapToJson(Map<?, ?> map) {
		return toJson(map);
	}

	/**
	 * Map对象转JSON字符串
	 * @param list Java List对象
	 * @return 对应的JSON字符串
	 */
	public static String listToJson(List<?> list) {
		return toJson(list);
	}
	/*=========================================JSON字符串转换为FastJSON2内置类型==========================================*/

	/**
	 * 解析JSON字符串为JSONObject对象
	 * @param content JSON字符串
	 * @return JSONObject/JSONArray
	 */
	public static JSONObject toJSONObject(String content) {
		return JSON.parseObject(content);
	}

	/**
	 * JSON字符串转JSONArray对象（原始JSON数组）
	 * @param json JSON数组字符串
	 * @return JSONArray对象，可直接操作JSON元素
	 */
	public static JSONArray toJsonArray(String json) {
		return JSON.parseArray(json);
	}

	/*===========================================JSON字符串转换为Java类型================================================*/

	/**
	 * JSON字符串转Java对象（最简版）
	 * @param json  有效的JSON字符串
	 * @param clazz 目标Java类型
	 * @return 转换后的Java对象
	 */
	public static <T> T toJavaObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * JSON字符串转Java对象（支持复杂泛型）
	 * @param json JSON字符串
	 * @param type 目标类型（用createGenericType构造）
	 * @return 转换后的Java对象
	 */
	public static <T> T toJavaObject(String json, Type type) {
		return JSON.parseObject(json, type);
	}

	/**
	 * JSON字符串转List集合（元素为特定类型）
	 * @param json  JSON数组字符串
	 * @param clazz List中元素的类型
	 * @return 转换后的List集合
	 */
	public static <T> List<T> toList(String json, Class<T> clazz) {
		return JSON.parseArray(json, clazz);
	}

	/**
	 * JSON字符串转通用List集合
	 * @param json JSON数组字符串
	 * @return List<Object>集合（元素类型自动推断）
	 */
	public static List<Object> toList(String json) {
		return JSON.parseArray(json);
	}

	/**
	 * JSON字符串转Map集合（值类型自动推断）
	 * @param json JSON对象字符串
	 * @return Map<String, Object>集合
	 */
	public static Map<String, Object> toMap(String json) {
		return JSON.parseObject(json,
				createGenericType(Map.class, String.class, Object.class));
	}

	/**
	 * JSON字符串转Map集合（值类型指定）
	 * @param json  JSON对象字符串
	 * @param valueClazz Map值类型
	 * @return Map<String, ValueType>集合
	 */
	public static <V> Map<String, V> toMap(String json, Class<V> valueClazz) {
		return JSON.parseObject(json,
				createGenericType(Map.class, String.class, valueClazz));
	}

	/*===================================================从JSON对象中获取元素============================================*/

	/**
	 * 获取JSON对象中的元素
	 * @param node JSONObject节点
	 * @param name 字段名称
	 * @return 对应字段值
	 */
	public static Object getJsonElement(JSONObject node, String name) {
		return node.get(name);
	}

	/**
	 * 获取JSON数组中的元素
	 * @param node JSONArray节点
	 * @param index 索引位置
	 * @return 对应位置元素
	 */
	public static Object getJsonElement(JSONArray node, int index) {
		return node.get(index);
	}

	/*=====================================================其他方法=====================================================*/

	/*
	 * 过滤敏感字段
	 * @param obj
	 * @return java.lang.String
	 * @author ren
	 * @date 2025/05/20 15:13
	 */
	public static String filterSensitiveFields(String[] excludeFields,Object... args) {
		//过滤掉请求参数中的Servlet对象,防止序列化，如果强行序列化会报错
		List<Object> safeArgs = new ArrayList<>();
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof MultipartFile) {
				continue;
			}
			safeArgs.add(arg);
		}
		// 创建一个SimplePropertyPreFilter对象，并设置需要过滤的字段名
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		// filter.getExcludes()获取到的是一个Set集合，将上方需要过滤字段名数组转换为Set并添加到其中
		filter.getExcludes().addAll(Arrays.asList(excludeFields));

		String jsonStr = JSON.toJSONString(safeArgs, filter);

		//JSON.toJSONString的一种特殊用法，将传进来的Object对象转换成JSON字符串，并使用SimplePropertyPreFilter过滤掉指定的字段
		return jsonStr;
	}
}