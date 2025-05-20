package com.ren.common.utils;

import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import com.alibaba.fastjson2.util.TypeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

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
	 * 构造参数化类型
	 * @param rawType 原始类型（如List.class）
	 * @param typeArguments 类型参数（如String.class）
	 * @return ParameterizedType实例
	 */
	public static ParameterizedType makeJavaType(Type rawType, Type... typeArguments) {
		return new ParameterizedTypeImpl(typeArguments, null, rawType);
	}

	/**
	 * 将对象转换为字符串
	 * @param value 输入对象
	 * @return 字符串结果（null安全）
	 */
	public static String toString(Object value) {
		if (Objects.isNull(value)) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return toJSONString(value);
	}

	/**
	 * 对象转JSON字符串
	 * @param value 要转换的对象
	 * @return JSON字符串
	 */
	public static String toJSONString(Object value) {
		return JSON.toJSONString(value);
	}

	/**
	 * 生成格式化的JSON字符串
	 * @param value 要转换的对象
	 * @return 美化后的JSON字符串
	 */
	public static String toPrettyString(Object value) {
		return JSON.toJSONString(value, JSONWriter.Feature.PrettyFormat);
	}

	/**
	 * 将Java对象转换为JSON元素
	 * @param value 输入对象
	 * @return JSONObject/JSONArray/null
	 */
	public static Object fromJavaObject(Object value) {
		Object result = null;
		if (Objects.nonNull(value) && (value instanceof String)) {
			result = parseObject((String) value);
		} else {
			result = JSON.toJSON(value);
		}
		return result;
	}

	/**
	 * 解析JSON字符串为通用JSON对象
	 * @param content JSON字符串
	 * @return JSONObject/JSONArray
	 */
	public static Object parseObject(String content) {
		return JSON.parseObject(content, Object.class);
	}

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

	/**
	 * 将JSON元素转换为Java对象
	 * @param node JSON元素（JSONObject/JSONArray）
	 * @param clazz 目标类型
	 * @return 转换后的Java对象
	 */
	public static <T> T toJavaObject(Object node, Class<T> clazz) {
		return JSON.to(clazz, node);
	}

	/**
	 * 将JSON元素转换为指定类型的Java对象（支持泛型）
	 * @param node JSON元素（JSONObject/JSONArray/String）
	 * @param type 目标类型
	 * @return 转换后的Java对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toJavaObject(@NonNull Object node, Type type) {
		if (node instanceof JSONObject) {
			return ((JSONObject) node).to(type);
		}
		if (node instanceof JSONArray) {
			return ((JSONArray) node).to(type);
		}
		if (node instanceof String) {
			return JSON.parseObject((String) node, type);
		}
		return (T) TypeUtils.cast(node, TypeUtils.getClass(type));
	}

	/**
	 * 将JSON元素转换为TypeReference指定类型的Java对象
	 * @param node JSON元素（JSONObject/JSONArray/String）
	 * @param typeReference 类型引用
	 * @return 转换后的Java对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toJavaObject(@NonNull Object node, TypeReference<T> typeReference) {
		if (node instanceof JSONObject) {
			return typeReference.to((JSONObject) node);
		}
		if (node instanceof JSONArray) {
			return typeReference.to((JSONArray) node);
		}
		if (node instanceof String) {
			return JSON.parseObject((String) node, typeReference);
		}
		return (T) TypeUtils.cast(node, typeReference.getRawType());
	}

	/**
	 * 将JSON元素转换为List集合
	 * @param node JSON元素
	 * @param clazz 元素类型
	 * @return List集合
	 */
	public static <E> List<E> toJavaList(Object node, Class<E> clazz) {
		return toJavaObject(node, makeJavaType(List.class, clazz));
	}

	/**
	 * 将JSON元素转换为通用List集合
	 * @param node JSON元素
	 * @return List<Object>集合
	 */
	public static List<Object> toJavaList(Object node) {
		return toJavaObject(node, new TypeReference<List<Object>>(){});
	}

	/**
	 * 将JSON元素转换为Map集合
	 * @param node JSON元素
	 * @param clazz 值类型
	 * @return Map<String, ValueType>集合
	 */
	public static <V> Map<String, V> toJavaMap(Object node, Class<V> clazz) {
		return toJavaObject(node, makeJavaType(Map.class, String.class, clazz));
	}

	/**
	 * 将JSON元素转换为通用Map集合
	 * @param node JSON元素
	 * @return Map<String, Object>集合
	 */
	public static Map<String, Object> toJavaMap(Object node) {
		return toJavaObject(node, new TypeReference<Map<String, Object>>(){});
	}

	/**
	 * 将JSON字符串转换为Java对象
	 * @param content JSON字符串
	 * @param clazz 目标类型
	 * @return Java对象
	 */
	public static <T> T toJavaObject(String content, Class<T> clazz) {
		return JSON.parseObject(content, clazz);
	}

	/**
	 * 将JSON字符串转换为指定类型的Java对象
	 * @param content JSON字符串
	 * @param type 目标类型
	 * @return Java对象
	 */
	public static <T> T toJavaObject(String content, Type type) {
		return JSON.parseObject(content, type);
	}

	/**
	 * 将JSON字符串转换为TypeReference指定类型的Java对象
	 * @param content JSON字符串
	 * @param typeReference 类型引用
	 * @return Java对象
	 */
	public static <T> T toJavaObject(String content, TypeReference<T> typeReference) {
		return JSON.parseObject(content, typeReference);
	}

	/**
	 * 将JSON字符串转换为List集合
	 * @param content JSON字符串
	 * @param clazz 元素类型
	 * @return List集合
	 */
	public static <E> List<E> toJavaList(String content, Class<E> clazz) {
		return JSON.parseObject(content, makeJavaType(List.class, clazz));
	}

	/**
	 * 将JSON字符串转换为通用List集合
	 * @param content JSON字符串
	 * @return List<Object>集合
	 */
	public static List<Object> toJavaList(String content) {
		return JSON.parseObject(content, new TypeReference<List<Object>>(){});
	}

	/**
	 * 将JSON字符串转换为Map集合
	 * @param content JSON字符串
	 * @param clazz 值类型
	 * @return Map<String, ValueType>集合
	 */
	public static <V> Map<String, V> toJavaMap(String content, Class<V> clazz) {
		return JSON.parseObject(content, makeJavaType(Map.class, String.class, clazz));
	}

	/**
	 * 将JSON字符串转换为通用Map集合
	 * @param content JSON字符串
	 * @return Map<String, Object>集合
	 */
	public static Map<String, Object> toJavaMap(String content) {
		return JSON.parseObject(content, new TypeReference<Map<String, Object>>(){});
	}

	/*
	 * 过滤敏感字段
	 * @param obj
	 * @return java.lang.String
	 * @author admin
	 * @date 2025/05/20 15:13
	 */
	public static String filterSensitiveFields(String[] excludeFields,Object... args) {
		//过滤掉请求参数中的Servlet对象,防止序列化，如果强行序列化会报错
		List<Object> safeArgs = new ArrayList<>();
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
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