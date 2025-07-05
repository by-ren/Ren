package com.ren.common.properties;

import java.util.List;

import lombok.Data;

/**
 * ModuleGroupProperties Swagger分组属性
 *
 * @author ren
 * @version 2025/07/02 11:04
 **/
@Data
public class ModuleGroupProperties {
    private String name; // 分组名称
    private MatchMode matchMode; // 匹配模式
    private List<String> paths; // 路径匹配规则列表
    private List<String> packages; // 包匹配规则列表

    private String title;         // API 标题
    private String description;   // API 描述
    private String version;       // API 版本
    private String contactName;   // 联系人姓名
    private String contactEmail;  // 联系人邮箱
    private String contactUrl;    // 联系人URL
    private String licenseName;   // 许可证名称
    private String licenseUrl;    // 许可证URL

    public enum MatchMode {
        PATH, PACKAGE
    }
}