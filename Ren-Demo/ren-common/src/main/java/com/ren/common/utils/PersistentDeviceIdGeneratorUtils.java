package com.ren.common.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistentDeviceIdGeneratorUtils {

    /**
     * 生成稳定不变的设备ID（相同设备+相同浏览器ID永远相同）
     *
     * @param request HTTP请求对象
     * @return 设备ID哈希值（32字符的SHA-256）
     */
    public static String generate(HttpServletRequest request) {
        // 1. 收集尽可能多的设备特征信息
        DeviceFeatures features = collectDeviceFeatures(request);

        // 2. 组合特征值创建唯一的特征字符串
        String featureSet = features.toString();

        // 3. 生成SHA-256哈希作为设备ID
        return sha256Hash(featureSet);
    }

    /**
     * 收集设备的关键不变特征（用于生成设备ID）
     */
    private static DeviceFeatures collectDeviceFeatures(HttpServletRequest request) {
        DeviceFeatures features = new DeviceFeatures();

        // 核心不变特征
        features.add("userAgent", request::getHeader, "User-Agent");
        features.add("accept", request::getHeader, "Accept");
        features.add("acceptLanguage", request::getHeader, "Accept-Language");
        features.add("acceptEncoding", request::getHeader, "Accept-Encoding");

        // 现代浏览器的稳定特性（需浏览器支持）
        try {
            features.add("secChUa", request::getHeader, "Sec-Ch-Ua");
            features.add("secChUaPlatform", request::getHeader, "Sec-Ch-Ua-Platform");
            features.add("secChUaMobile", request::getHeader, "Sec-Ch-Ua-Mobile");
        } catch (Exception ignored) {}

        // 硬件相关特性
        features.add("deviceMemory", request::getHeader, "Device-Memory");
        features.add("hardwareConcurrency", request::getHeader, "Hardware-Concurrency");

        return features;
    }

    /**
     * SHA-256哈希算法（确保设备ID不可逆）
     */
    private static String sha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * 设备特征收集器
     */
    static class DeviceFeatures {
        private final StringJoiner joiner = new StringJoiner("|");

        public void add(String key, HeaderSupplier supplier, String headerName) {
            String value = supplier.getHeader(headerName);
            if (value != null && !value.isEmpty()) {
                // 对值进行简化处理增加稳定性
                String stableValue = value.replaceAll("\\d+\\.\\d+", "X")
                                         .replaceAll("\\s+", "");
                joiner.add(key + "=" + stableValue);
            }
        }

        @Override
        public String toString() {
            // 按字母排序确保结果稳定
            return Stream.of(joiner.toString().split("\\|"))
                    .sorted()
                    .collect(Collectors.joining("|"));
        }
    }

    @FunctionalInterface
    interface HeaderSupplier {
        String getHeader(String name);
    }
}