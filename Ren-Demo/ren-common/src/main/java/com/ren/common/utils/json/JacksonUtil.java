package com.ren.common.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 增强版 JSON 工具类（线程安全）
 * 整合功能：
 * 1. 支持 Java 对象与 JSON 字符串/文件互转
 * 2. 支持复杂泛型类型反序列化
 * 3. 支持 JSON 树模型（JsonNode）操作
 * 4. 支持 Java 8 时间类型序列化
 * 5. 提供开发/生产环境配置（如美化输出）
 *
 * 配置说明：
 * - 日期格式: yyyy-MM-dd HH:mm:ss
 * - 时区: Asia/Shanghai
 * - 忽略未知属性 | 允许单引号 | 空字符串转null
 * - 序列化排除 null 字段
 */
@Slf4j
public class JacksonUtil {

	// 线程安全的 ObjectMapper（Jackson 2.10+ 已保证线程安全）
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		//------------------------ 序列化配置 ------------------------
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 忽略null字段
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);              // 开发环境美化输出
		//objectMapper.disable(SerializationFeature.INDENT_OUTPUT);           // 生产环境关闭美化
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		//------------------------ 反序列化配置 ----------------------
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 忽略未知字段
		objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);             // 允许单引号
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT); // 空字符串转null

		//------------------------ 其他配置 --------------------------
		objectMapper.registerModule(new JavaTimeModule());                     // 支持Java8时间类型
		objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));       // 设置时区
	}

	/** ======================== 序列化方法 ======================== */

	/**
	 * 对象转JSON字符串
	 * @param object 可序列化对象（包括集合、数组）
	 * @return 失败返回null，不会抛出异常
	 */
	public static String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Object转JSON失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 对象转美化格式JSON（开发调试用）
	 */
	public static String toPrettyJson(Object object) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Object转美化JSON失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 对象转字节数组（用于网络传输）
	 */
	public static byte[] toBytes(Object object) {
		try {
			return objectMapper.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			log.error("Object转Byte数组失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 对象写入文件
	 * @param file 目标文件（不存在会自动创建）
	 */
	public static void writeToFile(File file, Object object) {
		try {
			objectMapper.writeValue(file, object);
		} catch (IOException e) {
			log.error("写入JSON文件失败: {}", e.getMessage());
		}
	}

	/** ======================== 反序列化方法 ======================== */

	/**
	 * JSON字符串转对象（基础类型）
	 * @param json  合规JSON字符串
	 * @param clazz 目标类型（不支持泛型）
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			log.error("JSON转Object失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * JSON字符串转复杂对象（支持泛型）
	 * 示例：List<User> list = fromJson(json, new TypeReference<>() {});
	 */
	public static <T> T fromJson(String json, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (JsonProcessingException e) {
			log.error("JSON转泛型对象失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 从JSON文件读取对象
	 * @param file  包含JSON数据的文件
	 * @param clazz 目标类型
	 */
	public static <T> T readFromFile(File file, Class<T> clazz) {
		try {
			return objectMapper.readValue(file, clazz);
		} catch (IOException e) {
			log.error("读取JSON文件失败: {}", e.getMessage());
			return null;
		}
	}

	/** ======================== JsonNode操作 ======================== */

	/**
	 * 字符串转JsonNode树
	 */
	public static JsonNode parseTree(String json) {
		try {
			return objectMapper.readTree(json);
		} catch (JsonProcessingException e) {
			log.error("JSON解析为树失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 对象转JsonNode树
	 */
	public static JsonNode toTree(Object object) {
		return objectMapper.valueToTree(object);
	}

	/**
	 * 创建空JSON对象节点
	 */
	public static ObjectNode createObjectNode() {
		return objectMapper.createObjectNode();
	}

	/**
	 * 创建空JSON数组节点
	 */
	public static ArrayNode createArrayNode() {
		return objectMapper.createArrayNode();
	}

	/** ======================== 高级功能 ======================== */

	/**
	 * 获取ObjectMapper副本（用于特殊配置）
	 * 注意：修改副本不会影响全局配置
	 */
	public static ObjectMapper copyMapper() {
		return objectMapper.copy();
	}
}