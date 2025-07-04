package com.ren.framework.config;

import java.util.*;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ren.common.properties.ModuleGroupProperties;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Swagger配置类 - 实现动态分组功能
 *
 * 主要功能： 1. 通过配置文件定义多个API分组 2. 每个分组可独立设置名称、标题、描述、版本等OpenAPI信息 3. 支持路径匹配和包匹配两种分组方式
 *
 * @author ren
 * @version 2025/07/02 10:59
 **/
@Configuration
public class SwaggerConfig implements EnvironmentAware {

    private Environment environment;

    /**
     * 设置环境对象，用于后续配置绑定
     *
     * @param environment Spring环境对象，提供访问配置属性的能力
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 配置分组容器类 - 用于绑定配置文件中的分组列表
     *
     * 配置文件结构示例： modules: groups: - name: 用户模块 matchMode: PATH paths: [/user/**] title: 用户管理
     */
    @Data
    public static class ModuleGroups {
        private List<ModuleGroupProperties> groups;
        private String basePath = "";
    }

    /**
     * 默认分组（关键：确保分组功能激活）
     *
     * 作用： 1. 作为分组功能的激活Bean（解决show-actuator=false时分组不显示的问题） 2. 收集未被其他分组包含的接口 3. 提供默认的API信息
     *
     * @return GroupedOpenApi 默认分组对象
     */
    @Bean
    public GroupedOpenApi defaultGroup() {
        // 获取需要排除的路径和包
        HashMap<String, Set<String>> allExcludedPathAndPackage = getExcludedPathAndPackage();
        Set<String> allExcludedPaths = allExcludedPathAndPackage.get("allExcludedPaths");
        Set<String> allExcludedPackages = allExcludedPathAndPackage.get("allExcludedPackages");

        return GroupedOpenApi.builder().group("未分组接口") // 分组标识
            .displayName("未分组接口") // 在UI中显示的名称
            .pathsToExclude(allExcludedPaths.toArray(new String[0])) // 排除其他分组的路径
            .addOpenApiMethodFilter(method -> {
                // 排除其他分组的包
                if (!allExcludedPackages.isEmpty()) {
                    String packageName = method.getDeclaringClass().getPackageName();
                    return allExcludedPackages.stream().noneMatch(packageName::startsWith);
                }
                return true; // 不在排除包中则保留
            }).addOpenApiMethodFilter(method -> method.isAnnotationPresent(Operation.class)) // 过滤条件：只包含标注@Operation的接口
            .addOpenApiCustomizer(
                openApi -> openApi.info(new Info().title("未分组接口").description("其他未归类的接口").version("1.0")))
            .build();// 自定义API信息
    }

    /**
     * 获取需要排除的路径和包
     * 
     * @return java.util.HashMap<java.lang.String,java.util.Set<java.lang.String>>
     * @author ren
     * @date 2025/07/03 11:06
     */
    private HashMap<String, Set<String>> getExcludedPathAndPackage() {
        // 获取配置文件中配置的所有组
        ModuleGroups moduleGroups =
            Binder.get(environment).bind("modules", Bindable.of(ModuleGroups.class)).orElse(new ModuleGroups());
        List<ModuleGroupProperties> groups = moduleGroups.getGroups();

        // 收集所有需要排除的路径和包
        Set<String> allExcludedPaths = new HashSet<>();
        Set<String> allExcludedPackages = new HashSet<>();
        if (groups != null) {
            for (ModuleGroupProperties group : groups) {
                // 收集排除规则
                if (group.getMatchMode() == ModuleGroupProperties.MatchMode.PATH) {
                    if (group.getPaths() != null) {
                        allExcludedPaths.addAll(group.getPaths());
                    }
                } else if (group.getMatchMode() == ModuleGroupProperties.MatchMode.PACKAGE) {
                    if (group.getPackages() != null) {
                        allExcludedPackages.addAll(group.getPackages());
                    }
                }
            }
        }

        return new HashMap<>() {
            {
                put("allExcludedPaths", allExcludedPaths);
                put("allExcludedPackages", allExcludedPackages);
            }
        };
    }

    /**
     * 动态注册Swagger分组Bean
     *
     * 主要流程： 1. 从环境变量中绑定分组配置 2. 为每个分组创建Bean定义 3. 将分组Bean注册到Spring容器
     *
     * @return BeanDefinitionRegistryPostProcessor 用于动态注册Bean的后处理器
     */
    @Bean
    public BeanDefinitionRegistryPostProcessor groupedOpenApiRegistrar() {
        return new BeanDefinitionRegistryPostProcessor() {
            /**
             * 注册Bean定义到Spring容器
             *
             * @param registry Spring Bean定义注册器
             */
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
                // 直接从环境变量绑定分组配置（避免依赖Bean初始化顺序）
                ModuleGroups moduleGroups =
                    Binder.get(environment).bind("modules", Bindable.of(ModuleGroups.class)).orElse(new ModuleGroups());

                // 处理配置中定义的分组
                if (moduleGroups.getGroups() != null) {
                    for (ModuleGroupProperties group : moduleGroups.getGroups()) {
                        // 创建Bean名称（替换空格为下划线）
                        String beanName = "groupedOpenApi_" + group.getName().replaceAll("\\s+", "_");

                        // 构建Bean定义（使用Supplier延迟创建实际对象）
                        BeanDefinition definition = BeanDefinitionBuilder
                            .genericBeanDefinition(GroupedOpenApi.class, () -> buildGroup(group, moduleGroups.getBasePath())).getBeanDefinition();

                        // 注册Bean定义到Spring容器
                        registry.registerBeanDefinition(beanName, definition);
                    }
                }
            }

            @Override
            public void postProcessBeanFactory(
                org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) {
                // 不需要实现
            }
        };
    }

    /**
     * 构建分组对象
     *
     * 根据配置创建GroupedOpenApi实例，包含： 1. 分组基本信息（名称、显示名） 2. API信息自定义器（标题、描述、版本等） 3. 接口匹配规则（路径匹配或包匹配）
     *
     * @param group 分组配置属性
     * @return GroupedOpenApi 分组对象
     */
    private GroupedOpenApi buildGroup(ModuleGroupProperties group, String basePath) {
        // 创建分组构建器
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder().group(group.getName()) // 分组标识
            .displayName(group.getName()) // 在UI中显示的名称
            .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Operation.class));// 过滤条件：只包含标注了@Operation的接口

        // 如果配置了API信息，添加自定义器
        if (hasApiInfo(group)) {
            builder.addOpenApiCustomizer(createInfoCustomizer(group,basePath));
        }

        // 根据匹配模式配置接口扫描规则
        switch (group.getMatchMode()) {
            case PATH:
                // 路径匹配模式：配置路径列表
                if (group.getPaths() != null && !group.getPaths().isEmpty()) {
                    builder.pathsToMatch(group.getPaths().toArray(new String[0]));
                }
                break;

            case PACKAGE:
                // 包匹配模式：配置包列表
                if (group.getPackages() != null && !group.getPackages().isEmpty()) {
                    builder.packagesToScan(group.getPackages().toArray(new String[0]));
                }
                break;
        }

        return builder.build();
    }

    /**
     * 检查分组是否配置了API信息
     *
     * 判断条件：标题、描述、版本、联系人等至少有一项不为空
     *
     * @param group 分组配置属性
     * @return boolean 是否包含API信息
     */
    private boolean hasApiInfo(ModuleGroupProperties group) {
        return group.getTitle() != null || group.getDescription() != null || group.getVersion() != null
            || group.getContactName() != null;
    }

    /**
     * 创建API信息自定义器
     *
     * 根据分组配置创建OpenAPI的Info对象，包含： 1. 标题、描述、版本 2. 联系人信息（姓名、邮箱、URL） 3. 许可证信息
     *
     * @param group 分组配置属性
     * @return OpenApiCustomizer API信息自定义器
     */
    private OpenApiCustomizer createInfoCustomizer(ModuleGroupProperties group, String basePath) {
        return openApi -> {
            // 创建Info对象
            Info info = new Info();

            // 设置基本信息
            if (group.getTitle() != null)
                info.setTitle(group.getTitle());
            if (group.getDescription() != null)
                info.setDescription(group.getDescription());
            if (group.getVersion() != null)
                info.setVersion(group.getVersion());

            // 设置联系人信息
            if (group.getContactName() != null || group.getContactEmail() != null || group.getContactUrl() != null) {
                Contact contact = new Contact();
                if (group.getContactName() != null)
                    contact.setName(group.getContactName());
                if (group.getContactEmail() != null)
                    contact.setEmail(group.getContactEmail());
                if (group.getContactUrl() != null)
                    contact.setUrl(group.getContactUrl());
                info.setContact(contact);
            }

            // 设置许可证信息
            if (group.getLicenseName() != null || group.getLicenseUrl() != null) {
                License license = new License();
                if (group.getLicenseName() != null)
                    license.setName(group.getLicenseName());
                if (group.getLicenseUrl() != null)
                    license.setUrl(group.getLicenseUrl());
                info.setLicense(license);
            }
            
            // 配置安全方案（在请求头传递 Token）
            // openApi.components(new Components().addSecuritySchemes("x-token",
            // new SecurityScheme().type(SecurityScheme.Type.APIKEY) // 认证类型为 API KEY
            // .in(SecurityScheme.In.HEADER) // Token 放在请求头中
            // .name("x-token") // 请求头的名称
            // .description("用户Token"))); // 描述

            // 获取原有的Paths对象
            Paths originalPaths = openApi.getPaths();
            // 创建一个新的Paths对象来存储带前缀的路径
            Paths prefixedPaths = new Paths();
            // 遍历所有路径，添加前缀并放入新的Paths对象
            if (originalPaths != null) {
                originalPaths.forEach((path, item) ->
                        prefixedPaths.addPathItem(basePath + path, item)
                );
            }
            // 设置新的Paths对象到OpenAPI文档
            openApi.setPaths(prefixedPaths);

            // 将Info对象设置到OpenAPI
            openApi.setInfo(info);
        };
    }
}