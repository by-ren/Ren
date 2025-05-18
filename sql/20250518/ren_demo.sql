/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80019 (8.0.19)
 Source Host           : localhost:3306
 Source Schema         : ren_demo

 Target Server Type    : MySQL
 Target Server Version : 80019 (8.0.19)
 File Encoding         : 65001

 Date: 18/05/2025 16:01:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `is_system` tinyint(1) NULL DEFAULT 1 COMMENT '是否系统内置（1：是，0：否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 1, 'admin', 20250419081637, '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 1, 'admin', 20250419081637, '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 1, 'admin', 20250419081637, '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 1, 'admin', 20250419081637, '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 1, 'admin', 20250419081637, '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 1, 'admin', 20250419081637, '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `is_stop` tinyint(1) NULL DEFAULT 0 COMMENT '是否停用（0：否 1：是）',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：否 1：是）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 203 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '任科技', 0, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1746783265, '任科技');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1745934956, '深圳总公司');
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1745934956, '长沙分公司');
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1745934956, '研发部门');
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1747103566, '市场部门');
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1745934956, '测试部门');
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1745934956, '财务部门');
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1746804992, '运维部门');
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1745934956, '市场部门');
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '任', '15888888888', 'ry@qq.com', 0, 0, 'admin', 1745934956, 'admin', 1746783297, '财务部门');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint(1) NULL DEFAULT 1 COMMENT '是否默认（1：是，0：否）',
  `is_stop` tinyint(1) NULL DEFAULT 0 COMMENT '是否停用（1：是，0：否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 1, 0, 'admin', 20250419081637, '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 0, 0, 'admin', 20250419081637, '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 0, 0, 'admin', 20250419081637, '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 1, 0, 'admin', 20250419081637, '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 1, 0, 'admin', 20250419081637, '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 1, 0, 'admin', 20250419081637, '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 1, 0, 'admin', 20250419081637, '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 0, 0, 'admin', 20250419081637, '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 1, 0, 'admin', 20250419081637, '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 1, 0, 'admin', 20250419081637, '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 0, 0, 'admin', 20250419081637, '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 1, 0, 'admin', 20250419081637, '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 0, 0, 'admin', 20250419081637, '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 0, 0, 'admin', 20250419081637, '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 0, 0, 'admin', 20250419081637, '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 0, 0, 'admin', 20250419081637, '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 0, 0, 'admin', 20250419081637, '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 0, 0, 'admin', 20250419081637, '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 0, 0, 'admin', 20250419081637, '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 0, 0, 'admin', 20250419081637, '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 0, 0, 'admin', 20250419081637, '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典编码',
  `is_stop` tinyint(1) NULL DEFAULT 0 COMMENT '是否停用（1：是，0：否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 0, 'admin', 20250419081637, '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', 0, 'admin', 20250419081637, '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', 0, 'admin', 20250419081637, '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', 0, 'admin', 20250419081637, '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', 0, 'admin', 20250419081637, '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', 0, 'admin', 20250419081637, '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', 0, 'admin', 20250419081637, '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', 0, 'admin', 20250419081637, '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', 0, 'admin', 20250419081637, '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', 0, 'admin', 20250419081637, '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `is_success` tinyint(1) NULL DEFAULT 0 COMMENT '是否登陆成功（1：是 0：否）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` bigint NULL DEFAULT NULL COMMENT '访问时间（时间戳：秒）',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`is_success` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 1, '验证码错误', 20250419082255);
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '登录成功', 20250419082301);
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '登录成功', 20250419094705);
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '登录成功', 20250423134912);
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '退出成功', 20250423134918);
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '登录成功', 20250423134956);
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 1, '验证码错误', 20250423142552);
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 1, 'Cannot invoke \"org.springframework.security.core.Authentication.getName()\" because \"usernamePasswordAuthenticationToken\" is null', 20250423142631);
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '登录成功', 20250423142711);
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '登录成功', 20250424154252);
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '127.0.0.1', '内网IP', 'Chrome 13', 'Windows 10', 0, '退出成功', 20250424154306);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` tinyint NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` tinyint NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `is_visible` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏（0：否 1：是）',
  `is_stop` tinyint(1) NULL DEFAULT 0 COMMENT '是否停用（0：否 1：是）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：否 1：是）',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', 'system', NULL, '', 1, 0, 'M', 0, 0, '', 'i-ep-setting', 'admin', 1745934956, 'admin', 1746866326, '系统管理目录', 0);
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', 'monitor', NULL, '', 1, 0, 'M', 0, 0, '', 'i-ep-dataanalysis', 'admin', 1745934956, 'admin', 1746866703, '系统监控目录', 0);
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', 'tool', NULL, '', 1, 0, 'M', 0, 0, '', 'i-ep-tools', 'admin', 1745934956, 'admin', 1746866851, '系统工具目录', 0);
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'user', 'user/Index', '', 1, 0, 'C', 0, 0, 'system:user:list', 'i-ep-user', 'admin', 1745934956, 'admin', 1746866343, '用户管理菜单', 0);
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'role', 'role/Index', '', 1, 0, 'C', 0, 0, 'system:role:list', 'i-ep-avatar', 'admin', 1745934956, 'admin', 1746866364, '角色管理菜单', 0);
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'menus', 'menus/Index', '', 1, 0, 'C', 0, 0, 'system:menu:list', 'i-ep-menu', 'admin', 1745934956, 'admin', 1746866374, '菜单管理菜单', 0);
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'dept', 'dept/Index', '', 1, 0, 'C', 0, 0, 'system:dept:list', 'i-ep-grid', 'admin', 1745934956, 'admin', 1746866466, '部门管理菜单', 0);
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'post', 'system/post/index', '', 1, 0, 'C', 0, 0, 'system:post:list', 'i-ep-postcard', 'admin', 1745934956, 'admin', 1746866490, '岗位管理菜单', 0);
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'dict', 'system/dict/index', '', 1, 0, 'C', 0, 0, 'system:dict:list', 'i-ep-notebook', 'admin', 1745934956, 'admin', 1746866522, '字典管理菜单', 0);
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'config', 'system/config/index', '', 1, 0, 'C', 0, 0, 'system:config:list', 'i-ep-edit', 'admin', 1745934956, 'admin', 1746866561, '参数设置菜单', 0);
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'notice', 'system/notice/index', '', 1, 0, 'C', 0, 0, 'system:notice:list', 'i-ep-mutenotification', 'admin', 1745934956, 'admin', 1746866576, '通知公告菜单', 0);
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', 'log', '', '', 1, 0, 'M', 0, 0, '', 'i-ep-editpen', 'admin', 1745934956, 'admin', 1746866599, '日志管理菜单', 0);
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'online', 'monitor/online/index', '', 1, 0, 'C', 0, 0, 'monitor:online:list', 'i-ep-coordinate', 'admin', 1745934956, 'admin', 1746866737, '在线用户菜单', 0);
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'job', 'monitor/job/index', '', 1, 0, 'C', 0, 0, 'monitor:job:list', 'i-ep-dishdot', 'admin', 1745934956, 'admin', 1746866790, '定时任务菜单', 0);
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'druid', 'monitor/druid/index', '', 1, 0, 'C', 0, 0, 'monitor:druid:list', 'i-ep-dataline', 'admin', 1745934956, 'admin', 1746866808, '数据监控菜单', 0);
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'server', 'monitor/server/index', '', 1, 0, 'C', 0, 0, 'monitor:server:list', 'i-ep-service', 'admin', 1745934956, 'admin', 1746866818, '服务监控菜单', 0);
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'cache', 'monitor/cache/index', '', 1, 0, 'C', 0, 0, 'monitor:cache:list', 'i-ep-maplocation', 'admin', 1745934956, 'admin', 1746866833, '缓存监控菜单', 0);
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'cacheList', 'monitor/cache/list', '', 1, 0, 'C', 0, 0, 'monitor:cache:list', 'i-ep-locationinformation', 'admin', 1745934956, 'admin', 1746866840, '缓存列表菜单', 0);
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'build', 'tool/build/index', '', 1, 0, 'C', 0, 0, 'tool:build:list', 'i-ep-document', 'admin', 1745934956, 'admin', 1746866925, '表单构建菜单', 0);
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'gen', 'tool/gen/index', '', 1, 0, 'C', 0, 0, 'tool:gen:list', 'i-ep-notification', 'admin', 1745934956, 'admin', 1746866939, '代码生成菜单', 0);
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'swagger', 'tool/swagger/index', '', 1, 0, 'C', 0, 0, 'tool:swagger:list', 'i-ep-takeawaybox', 'admin', 1745934956, 'admin', 1747103223, '系统接口菜单', 0);
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', 0, 0, 'monitor:operlog:list', 'i-ep-platform', 'admin', 1745934956, 'admin', 1746866625, '操作日志菜单', 0);
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', 0, 0, 'monitor:logininfor:list', 'i-ep-iphone', 'admin', 1745934956, 'admin', 1746866664, '登录日志菜单', 0);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` tinyint(1) NOT NULL COMMENT '公告类型（1：通知 2：公告）',
  `notice_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公告内容',
  `is_close` tinyint(1) NULL DEFAULT 0 COMMENT '是否关闭（1：是 0：否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', 2, '新版本内容', 0, 'admin', 20250419081637, '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', 1, '维护内容', 0, 'admin', 20250419081637, '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` tinyint NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` tinyint NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `is_normal` tinyint NULL DEFAULT 0 COMMENT '是否正常（1：是 0：否）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` bigint NULL DEFAULT NULL COMMENT '操作时间（时间戳：秒）',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`is_normal` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `is_stop` tinyint(1) NOT NULL COMMENT '是否停用（1：是，0：否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, 0, 'admin', 20250419081637, '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, 0, 'admin', 20250419081637, '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, 0, 'admin', 20250419081637, '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, 0, 'admin', 20250419081637, '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `is_stop` tinyint(1) NOT NULL COMMENT '是否停用（0：否 1：是）',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除（0：否 1：是）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `data_scope` tinyint(1) NULL DEFAULT 1 COMMENT '可查看数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5:仅本人数据权限）',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 0, 'admin', 1, 0, 'admin', 20250419081637, '', NULL, '超级管理员', 1);
INSERT INTO `sys_role` VALUES (2, '普通角色', 0, 'common', 3, 0, 'admin', 20250419081637, 'admin', 1747102588, '普通角色', 2);
INSERT INTO `sys_role` VALUES (109, '管理员', 0, 'administrators', 2, 0, 'admin', 1747054057, 'admin', 1747102631, '管理员', 1);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 102);
INSERT INTO `sys_role_dept` VALUES (2, 103);
INSERT INTO `sys_role_dept` VALUES (2, 104);
INSERT INTO `sys_role_dept` VALUES (2, 105);
INSERT INTO `sys_role_dept` VALUES (2, 107);
INSERT INTO `sys_role_dept` VALUES (2, 108);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (109, 1);
INSERT INTO `sys_role_menu` VALUES (109, 2);
INSERT INTO `sys_role_menu` VALUES (109, 3);
INSERT INTO `sys_role_menu` VALUES (109, 100);
INSERT INTO `sys_role_menu` VALUES (109, 101);
INSERT INTO `sys_role_menu` VALUES (109, 102);
INSERT INTO `sys_role_menu` VALUES (109, 103);
INSERT INTO `sys_role_menu` VALUES (109, 104);
INSERT INTO `sys_role_menu` VALUES (109, 105);
INSERT INTO `sys_role_menu` VALUES (109, 106);
INSERT INTO `sys_role_menu` VALUES (109, 107);
INSERT INTO `sys_role_menu` VALUES (109, 108);
INSERT INTO `sys_role_menu` VALUES (109, 109);
INSERT INTO `sys_role_menu` VALUES (109, 110);
INSERT INTO `sys_role_menu` VALUES (109, 111);
INSERT INTO `sys_role_menu` VALUES (109, 112);
INSERT INTO `sys_role_menu` VALUES (109, 113);
INSERT INTO `sys_role_menu` VALUES (109, 114);
INSERT INTO `sys_role_menu` VALUES (109, 115);
INSERT INTO `sys_role_menu` VALUES (109, 116);
INSERT INTO `sys_role_menu` VALUES (109, 117);
INSERT INTO `sys_role_menu` VALUES (109, 500);
INSERT INTO `sys_role_menu` VALUES (109, 501);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0未删除 1删除）',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` tinyint(1) NULL DEFAULT 0 COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` bigint NULL DEFAULT NULL COMMENT '最后登录时间（时间戳）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_username_uindex`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超级管理员', '{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha', 0, 103, '00', 'ren@163.com', '18411111111', 0, '', '127.0.0.1', 1747553974, 'admin', 1745934956, 'admin', 1747553974, '超级管理员');
INSERT INTO `sys_user` VALUES (5, 'zhangsan', '张三', '{bcrypt}$2a$10$vJGLDCIgJz03bOTLjt98Eueg24A6cCxzYC9Ky8pPugdUqbky31nou', 0, 103, '00', 'ren@yeah.com', '18422222222', 1, '', '127.0.0.1', 1746351509, 'admin', 1746347572, 'admin', 1747055326, '张三');
INSERT INTO `sys_user` VALUES (6, 'wangwu', '王五', '{bcrypt}$2a$10$2DNzMqN2J/khioiX9fgHIuXUR0inkKk.aNyc8TGCZulRFxyNKERim', 0, 109, '00', 'ren@yeah.com', '18155555555', 2, '', '', NULL, 'admin', 1746436640, 'admin', 1747054083, '王五');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (5, 2);
INSERT INTO `sys_user_role` VALUES (5, 109);
INSERT INTO `sys_user_role` VALUES (6, 2);
INSERT INTO `sys_user_role` VALUES (6, 109);

SET FOREIGN_KEY_CHECKS = 1;
