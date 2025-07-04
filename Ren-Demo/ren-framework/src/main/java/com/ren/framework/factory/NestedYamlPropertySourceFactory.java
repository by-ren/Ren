package com.ren.framework.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class NestedYamlPropertySourceFactory implements PropertySourceFactory {

	/**
	 * 读取Yaml配置项
	 * 方式一：使用@ConfigurationProperties(prefix = "aliyun")加字段private String accessKeyId;进行匹配，匹配结果为：aliyun.access-key-id（但是注意，这种方式虽然最标准，但是如果配置文件中是读取环境变量，将不会成功，例如${ALIBABA_ACCESS_KEY_ID:}将会直接返回字符串,需要手动读取环境变量）
	 * 方式二：使用@Value("${aliyun.access-key-id}")进行匹配，匹配结果为：aliyun.access-key-id，可以直接将配置文件中的值读取后进行环境变量匹配
	 * 方式三：使用@Value("${access-key-id}")进行匹配，但是如果项目存在多个同名配置项，将会出问题，可以直接将配置文件中的值读取后进行环境变量匹配
	 * @author ren
	 * @date 2025/06/28 14:21
	 */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

        // 1. 使用 Spring Boot 官方加载器解析 YAML
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> sources = loader.load(resource.getResource().getFilename(), resource.getResource());

        if (sources.isEmpty()) {
            return null;
        }

        // 2. 合并所有属性到嵌套结构
        Map<String, Object> nestedProperties = new HashMap<>();
        for (PropertySource<?> source : sources) {
            if (source.getSource() instanceof Map) {
                flattenMap("", (Map<?, ?>)source.getSource(), nestedProperties);
            }
        }

        // 3. 同时提供两种访问方式
        return new MapPropertySource(resource.getResource().getFilename() + "-nested", nestedProperties);
    }

    // 递归展平嵌套Map
    private void flattenMap(String prefix, Map<?, ?> source, Map<String, Object> result) {
        source.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key.toString() : prefix + "." + key;

            if (value instanceof Map) {
                // 递归处理嵌套Map
                flattenMap(fullKey, (Map<?, ?>)value, result);
            } else {
                // 添加三种格式的属性名:
                // 1. 完整路径 (cloud.storage.aliyun.access-key-id)
                result.put(fullKey, value);

                // 2. 短名前缀 (aliyun.access-key-id)
                String[] parts = fullKey.split("\\.");
                if (parts.length > 1) {
                    String shortKey = String.join(".", parts[parts.length - 2], parts[parts.length - 1]);
                    result.putIfAbsent(shortKey, value);
                }

                // 3. 短名无前缀 (access-key-id)
                result.putIfAbsent(parts[parts.length - 1], value);
            }
        });
    }
}