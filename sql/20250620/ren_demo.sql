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

 Date: 20/06/2025 16:26:27
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
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 1, 'admin', 1747626140, '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '111111', 1, 'admin', 1747626140, '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 1, 'admin', 1747626140, '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 1, 'admin', 1747626140, '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 1, 'admin', 1747626140, '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 1, 'admin', 1747626140, '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

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
) ENGINE = InnoDB AUTO_INCREMENT = 203 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (101, 1, '系统用户', '00', 'sys-user-usertype', '', '', 1, 0, 'admin', 1747979985, 'admin', 1747981145, '系统用户');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典编码',
  `is_stop` tinyint(1) NULL DEFAULT 0 COMMENT '是否停用（1：是，0：否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间（时间戳：秒）',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间（时间戳：秒）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_type_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (101, '用户类型', 'sys-user-usertype', 0, 'admin', 1747979794, '', NULL, '用户类型');

-- ----------------------------
-- Table structure for sys_image_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_image_log`;
CREATE TABLE `sys_image_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'OSS存储桶名称',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'DEFAULT' COMMENT 'OSS返回的ETag',
  `belong` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文件大小(字节) ',
  `etag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '文件MIME类型',
  `file_size` bigint NULL DEFAULT 1 COMMENT '文件访问URL',
  `mime_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '云服务名称',
  `cloud_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '业务归属分类',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_image_log
-- ----------------------------
INSERT INTO `sys_image_log` VALUES (103, 'xueyaxuetang', 'https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/93abdb7abc9744f6a3b61896abfc185f.jpeg', 'xyxt_img/article', 'xyxt_img/article/93abdb7abc9744f6a3b61896abfc185f.jpeg', 'C564BA11C9603B7B906A8BF2306C926C', 252104, 'image/jpeg', 'ALIYUN', 1750391660);
INSERT INTO `sys_image_log` VALUES (104, 'xueyaxuetang', 'https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/b2f49a5046a84dfcbcc0e9f538e49932.jpeg', 'xyxt_img/article', 'xyxt_img/article/b2f49a5046a84dfcbcc0e9f538e49932.jpeg', 'C564BA11C9603B7B906A8BF2306C926C', 252104, 'image/jpeg', 'aliyun', 1750398169);
INSERT INTO `sys_image_log` VALUES (105, 'xueyaxuetang', 'https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/b52782f66f6a45299f94fa5b8ba90d6d.jpeg', 'xyxt_img/article', 'xyxt_img/article/b52782f66f6a45299f94fa5b8ba90d6d.jpeg', '1BEEDD14006F075907858EFA2A047EE3', 102874, 'image/jpeg', 'aliyun', 1750407475);

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
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747747030);
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747747193);
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792283);
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792314);
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792471);
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792492);
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792540);
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792582);
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792608);
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747792669);
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793225);
INSERT INTO `sys_logininfor` VALUES (122, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793253);
INSERT INTO `sys_logininfor` VALUES (123, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793599);
INSERT INTO `sys_logininfor` VALUES (124, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793607);
INSERT INTO `sys_logininfor` VALUES (125, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793639);
INSERT INTO `sys_logininfor` VALUES (126, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793647);
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793675);
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747793926);
INSERT INTO `sys_logininfor` VALUES (129, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747794008);
INSERT INTO `sys_logininfor` VALUES (130, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747798088);
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747798285);
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747798969);
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747799392);
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747799423);
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747799482);
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747805381);
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747805637);
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747807665);
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747808464);
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747808472);
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747808508);
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747808618);
INSERT INTO `sys_logininfor` VALUES (143, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747808687);
INSERT INTO `sys_logininfor` VALUES (144, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747884077);
INSERT INTO `sys_logininfor` VALUES (145, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747884089);
INSERT INTO `sys_logininfor` VALUES (146, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747884420);
INSERT INTO `sys_logininfor` VALUES (147, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747885398);
INSERT INTO `sys_logininfor` VALUES (148, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747969313);
INSERT INTO `sys_logininfor` VALUES (149, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1747972261);
INSERT INTO `sys_logininfor` VALUES (150, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '登录成功', 1750398726);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 10, 'system', 'system', NULL, '', 1, 0, 'M', 0, 0, '', 'i-ep-setting', 'admin', 1745934956, 'admin', 1746866326, '系统管理目录', 0);
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 20, 'monitor', 'monitor', NULL, '', 1, 0, 'M', 0, 0, '', 'i-ep-dataanalysis', 'admin', 1745934956, 'admin', 1746866703, '系统监控目录', 0);
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 30, 'tool', 'tool', NULL, '', 1, 0, 'M', 0, 0, '', 'i-ep-tools', 'admin', 1745934956, 'admin', 1746866851, '系统工具目录', 0);
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 10, 'user', 'user', 'system/user/Index', '', 1, 0, 'C', 0, 0, 'system:user:list', 'i-ep-user', 'admin', 1745934956, 'admin', 1746866343, '用户管理菜单', 0);
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 20, 'role', 'role', 'system/role/Index', '', 1, 0, 'C', 0, 0, 'system:role:list', 'i-ep-avatar', 'admin', 1745934956, 'admin', 1746866364, '角色管理菜单', 0);
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 30, 'menu', 'menus', 'system/menus/Index', '', 1, 0, 'C', 0, 0, 'system:menu:list', 'i-ep-menu', 'admin', 1745934956, 'admin', 1746866374, '菜单管理菜单', 0);
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 40, 'dept', 'dept', 'system/dept/Index', '', 1, 0, 'C', 0, 0, 'system:dept:list', 'i-ep-grid', 'admin', 1745934956, 'admin', 1746866466, '部门管理菜单', 0);
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 50, 'post', 'post', 'system/post/Index', '', 1, 0, 'C', 0, 0, 'system:post:list', 'i-ep-postcard', 'admin', 1745934956, 'admin', 1746866490, '岗位管理菜单', 0);
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 60, 'dict', 'dict', 'system/dict/Index', '', 1, 0, 'C', 0, 0, 'system:dict:list', 'i-ep-notebook', 'admin', 1745934956, 'admin', 1746866522, '字典管理菜单', 0);
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 70, 'config', 'config', 'system/config/Index', '', 1, 0, 'C', 0, 0, 'system:config:list', 'i-ep-edit', 'admin', 1745934956, 'admin', 1746866561, '参数设置菜单', 0);
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 80, 'notice', 'notice', 'system/notice/Index', '', 1, 0, 'C', 0, 0, 'system:notice:list', 'i-ep-mutenotification', 'admin', 1745934956, 'admin', 1746866576, '通知公告菜单', 0);
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 90, 'log', 'log', '', '', 1, 0, 'M', 0, 0, '', 'i-ep-editpen', 'admin', 1745934956, 'admin', 1746866599, '日志管理菜单', 0);
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 10, 'online', 'online', 'monitor/online/Index', '', 1, 0, 'C', 0, 0, 'monitor:online:list', 'i-ep-coordinate', 'admin', 1745934956, 'admin', 1746866737, '在线用户菜单', 0);
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2002, 20, 'task', 'task', 'system/task/Index', '', 1, 0, 'C', 0, 0, 'systemTask:task:list', 'i-ep-dishdot', 'admin', 1745934956, 'admin', 1750398992, '定时任务菜单', 0);
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 30, 'druid', 'druid', 'monitor/druid/Index', '', 1, 0, 'C', 0, 0, 'monitor:druid:list', 'i-ep-dataline', 'admin', 1745934956, 'admin', 1750399019, '数据监控菜单', 1);
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 40, 'server', 'server', 'monitor/server/Index', '', 1, 0, 'C', 0, 0, 'monitor:server:list', 'i-ep-service', 'admin', 1745934956, 'admin', 1746866818, '服务监控菜单', 0);
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 50, 'cache', 'cache', 'monitor/cache/Index', '', 1, 0, 'C', 0, 0, 'monitor:cache:list', 'i-ep-maplocation', 'admin', 1745934956, 'admin', 1746866833, '缓存监控菜单', 0);
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 60, 'cacheList', 'cacheList', 'monitor/cache/List', '', 1, 0, 'C', 0, 0, 'monitor:cache:List', 'i-ep-locationinformation', 'admin', 1745934956, 'admin', 1750399071, '缓存列表菜单', 0);
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 10, 'build', 'build', 'tool/build/Index', '', 1, 0, 'C', 0, 0, 'tool:build:list', 'i-ep-document', 'admin', 1745934956, 'admin', 1746866925, '表单构建菜单', 0);
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 20, 'gen', 'gen', 'tool/gen/Index', '', 1, 0, 'C', 0, 0, 'tool:gen:list', 'i-ep-notification', 'admin', 1745934956, 'admin', 1746866939, '代码生成菜单', 0);
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 30, 'swagger', 'swagger', 'tool/swagger/Index', '', 1, 0, 'C', 0, 0, 'tool:swagger:list', 'i-ep-takeawaybox', 'admin', 1745934956, 'admin', 1747103223, '系统接口菜单', 0);
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 10, 'operlog', 'operlog', 'system/operLog/Index', '', 1, 0, 'C', 0, 0, 'system:operlog:list', 'i-ep-platform', 'admin', 1745934956, 'admin', 1746866625, '操作日志菜单', 0);
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 20, 'logininfor', 'logininfor', 'system/logininfor/Index', '', 1, 0, 'C', 0, 0, 'system:logininfor:list', 'i-ep-iphone', 'admin', 1745934956, 'admin', 1746866664, '登录日志菜单', 0);
INSERT INTO `sys_menu` VALUES (2002, '系统任务', 0, 15, 'systemTask', '/systemTask', NULL, NULL, 0, 0, 'M', 0, 0, NULL, 'i-ep-basketball', 'admin', 1750398922, '', NULL, '', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2025-05-23 任系统新版本发布啦', 2, '新版本内容', 0, 'admin', 1747626140, '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2025-05-23 任系统凌晨维护', 1, '维护内容', 0, 'admin', 1747626140, '', NULL, '管理员');

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
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (102, '岗位模块', 1, 'com.ren.admin.controller.PostController.addPost()', 'POST', 1, 'admin', '', '/post/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"admin\"}],\"permissions\":[\"admin\"],\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"ren@163.com\",\"isDel\":0,\"loginDate\":1747741040,\"loginDateStr\":\"2025-05-20 19:37:20\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[{\"createBy\":\"admin\",\"createTime\":1747626140,\"createTimeStr\":\"2025-05-19 11:42:20\",\"dataScope\":1,\"isDel\":0,\"isStop\":0,\"remark\":\"超级管理员\",\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"updateBy\":\"\"}],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747741040,\"updateTimeStr\":\"2025-05-20 19:37:20\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747744141,\"isStop\":0,\"postCode\":\"123\",\"postId\":6,\"postName\":\"123\",\"postSort\":132,\"remark\":\"132\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 17);
INSERT INTO `sys_oper_log` VALUES (103, '岗位模块', 2, 'com.ren.admin.controller.PostController.modifyPost()', 'POST', 1, 'admin', '', '/post/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"admin\"}],\"permissions\":[\"admin\"],\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"ren@163.com\",\"isDel\":0,\"loginDate\":1747741040,\"loginDateStr\":\"2025-05-20 19:37:20\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[{\"createBy\":\"admin\",\"createTime\":1747626140,\"createTimeStr\":\"2025-05-19 11:42:20\",\"dataScope\":1,\"isDel\":0,\"isStop\":0,\"remark\":\"超级管理员\",\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"updateBy\":\"\"}],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747741040,\"updateTimeStr\":\"2025-05-20 19:37:20\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"isStop\":0,\"postCode\":\"123123123\",\"postId\":6,\"postName\":\"123\",\"postSort\":132,\"remark\":\"132\",\"updateBy\":\"admin\",\"updateTime\":1747744143}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 8);
INSERT INTO `sys_oper_log` VALUES (104, '岗位模块', 3, 'com.ren.admin.controller.PostController.postDelete()', 'DELETE', 1, 'admin', '', '/post/delete', '127.0.0.1', '内网IP', '[{\"postId\":\"6\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 7);
INSERT INTO `sys_oper_log` VALUES (105, '配置模块', 1, 'com.ren.admin.controller.ConfigController.addConfig()', 'POST', 1, 'admin', '', '/config/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"admin\"}],\"expireTime\":1747798282,\"permissions\":[\"admin\"],\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"ren@163.com\",\"isDel\":0,\"loginDate\":1747793926,\"loginDateStr\":\"2025-05-21 10:18:46\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[{\"createBy\":\"admin\",\"createTime\":1747626140,\"createTimeStr\":\"2025-05-19 11:42:20\",\"dataScope\":1,\"isDel\":0,\"isStop\":0,\"remark\":\"超级管理员\",\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"updateBy\":\"\"}],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747793926,\"updateTimeStr\":\"2025-05-21 10:18:46\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"configId\":101,\"configKey\":\"132\",\"configName\":\"123\",\"configValue\":\"132\",\"createBy\":\"admin\",\"createTime\":1747797989,\"isSystem\":0,\"remark\":\"132\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 10750);
INSERT INTO `sys_oper_log` VALUES (106, '配置模块', 3, 'com.ren.admin.controller.ConfigController.configDelete()', 'DELETE', 1, 'admin', '', '/config/delete', '127.0.0.1', '内网IP', '[{\"configId\":\"101\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 0);
INSERT INTO `sys_oper_log` VALUES (107, '配置模块', 1, 'com.ren.admin.controller.ConfigController.addConfig()', 'POST', 1, 'admin', '', '/config/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"admin\"}],\"expireTime\":1747798305,\"permissions\":[\"admin\"],\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"ren@163.com\",\"isDel\":0,\"loginDate\":1747793926,\"loginDateStr\":\"2025-05-21 10:18:46\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[{\"createBy\":\"admin\",\"createTime\":1747626140,\"createTimeStr\":\"2025-05-19 11:42:20\",\"dataScope\":1,\"isDel\":0,\"isStop\":0,\"remark\":\"超级管理员\",\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"updateBy\":\"\"}],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747793926,\"updateTimeStr\":\"2025-05-21 10:18:46\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"configId\":102,\"configKey\":\"123\",\"configName\":\"123\",\"configValue\":\"132\",\"createBy\":\"admin\",\"createTime\":1747798006,\"isSystem\":0,\"remark\":\"123\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 6043);
INSERT INTO `sys_oper_log` VALUES (108, '配置模块', 3, 'com.ren.admin.controller.ConfigController.configDelete()', 'DELETE', 1, 'admin', '', '/config/delete', '127.0.0.1', '内网IP', '[{\"configId\":\"102\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 3);
INSERT INTO `sys_oper_log` VALUES (109, '配置模块', 1, 'com.ren.admin.controller.ConfigController.addConfig()', 'POST', 1, 'admin', '', '/config/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"admin\"}],\"browser\":\"MSEdge\",\"expireTime\":1747798600,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747798279,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"admin\"],\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"ren@163.com\",\"isDel\":0,\"loginDate\":1747798088,\"loginDateStr\":\"2025-05-21 11:28:08\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[{\"createBy\":\"admin\",\"createTime\":1747626140,\"createTimeStr\":\"2025-05-19 11:42:20\",\"dataScope\":1,\"isDel\":0,\"isStop\":0,\"remark\":\"超级管理员\",\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"updateBy\":\"\"}],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747798088,\"updateTimeStr\":\"2025-05-21 11:28:08\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"configId\":103,\"configKey\":\"123\",\"configName\":\"123\",\"configValue\":\"123\",\"createBy\":\"admin\",\"createTime\":1747798303,\"isSystem\":0,\"remark\":\"123\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 7708);
INSERT INTO `sys_oper_log` VALUES (110, '配置模块', 1, 'com.ren.admin.controller.ConfigController.addConfig()', 'POST', 1, 'admin', '', '/config/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"admin\"}],\"browser\":\"MSEdge\",\"expireTime\":1747799380,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747798969,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"admin\"],\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImxvZ2luX3VzZXIiOiJ7XCJhdXRob3JpdGllc1wiOlt7XCJhdXRob3JpdHlcIjpcImFkbWluXCJ9XSxcImJyb3dzZXJcIjpcIk1TRWRnZVwiLFwiZXhwaXJlVGltZVwiOjE3NDc3OTkzODAsXCJpcGFkZHJcIjpcIjEyNy4wLjAuMVwiLFwibG9naW5Mb2NhdGlvblwiOlwi5YaF572RSVBcIixcImxvZ2luVGltZVwiOjE3NDc3OTg5NjksXCJvc1wiOlwiV2luZG93cyAxMCBvciBXaW5kb3dzIFNlcnZlciAyMDE2XCIsXCJwZXJtaXNzaW9uc1wiOltcImFkbWluXCJdLFwidXNlclwiOntcImF2YXRhclwiOlwiXCIsXCJjcmVhdGVCeVwiOlwiYWRtaW5cIixcImNyZWF0ZVRpbWVcIjoxNzQ1OTM0OTU2LFwiY3JlYXRlVGltZVN0clwiOlwiMjAyNS0wNC0yOSAyMTo1NTo1NlwiLFwiZGVwdFwiOntcImNoaWxkcmVuXCI6W10sXCJkZXB0SWRcIjoxMDN9LFwiZGVwdElkXCI6MTAzLFwiZW1haWxcIjpcInJlbkAxNjMuY29tXCIsXCJpc0RlbFwiOjAsXCJsb2dpbkRhdGVcIjoxNzQ3Nzk4Mjg1LFwibG9naW5EYXRlU3RyXCI6XCIyMDI1LTA1LTIxIDExOjMxOjI1XCIsXCJsb2dpbklwXCI6XCIxMjcuMC4wLjFcIixcIm5pY2tuYW1lXCI6XCLotoXnuqfnrqHnkIblkZhcIixcInBhc3N3b3JkXCI6XCJ7YmNyeXB0fSQyYSQxMCRyYnQyZVpIdjZSME1UQkRHVG5FODcuM21LRGdQM1pmaS5lY3ZFLzFKNG1ZSXRRSjZ3MkRoYVwiLFwicGhvbmVudW1iZXJcIjpcIjE4NDExMTExMTExXCIsXCJyZW1hcmtcIjpcIui2hee6p-euoeeQhuWRmFwiLFwicm9sZUxpc3RcIjpbe1wiY3JlYXRlQnlcIjpcImFkbWluXCIsXCJjcmVhdGVUaW1lXCI6MTc0NzYyNjE0MCxcImNyZWF0ZVRpbWVTdHJcIjpcIjIwMjUtMDUtMTkgMTE6NDI6MjBcIixcImRhdGFTY29wZVwiOjEsXCJpc0RlbFwiOjAsXCJpc1N0b3BcIjowLFwicmVtYXJrXCI6XCLotoXnuqfnrqHnkIblkZhcIixcInJvbGVJZFwiOjEsXCJyb2xlS2V5XCI6XCJhZG1pblwiLFwicm9sZU5hbWVcIjpcIui2hee6p-euoeeQhuWRmFwiLFwicm9sZVNvcnRcIjoxLFwidXBkYXRlQnlcIjpcIlwifV0sXCJzZXhcIjowLFwidXBkYXRlQnlcIjpcImFkbWluXCIsXCJ1cGRhdGVUaW1lXCI6MTc0Nzc5ODI4NSxcInVwZGF0ZVRpbWVTdHJcIjpcIjIwMjUtMDUtMjEgMTE6MzE6MjVcIixcInVzZXJJZFwiOjEsXCJ1c2VyVHlwZVwiOlwiMDBcIixcInVzZXJuYW1lXCI6XCJhZG1pblwifSxcInVzZXJJZFwiOjEsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0iLCJpYXQiOjE3NDc3OTkwNzAsImV4cCI6MTc0Nzc5OTM4MH0.GW1j_zOLFIhhMLjH2_8vqdzP3W3MgumYfDKAeTN', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 17);
INSERT INTO `sys_oper_log` VALUES (111, '配置模块', 3, 'com.ren.admin.controller.ConfigController.configDelete()', 'DELETE', 1, 'admin', '', '/config/delete', '127.0.0.1', '内网IP', '[{\"configId\":\"104\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 4945);
INSERT INTO `sys_oper_log` VALUES (112, '配置模块', 3, 'com.ren.admin.controller.ConfigController.configDelete()', 'DELETE', 1, 'admin', '', '/config/delete', '127.0.0.1', '内网IP', '[{\"configId\":\"103\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 6123);
INSERT INTO `sys_oper_log` VALUES (113, '通知公告模块', 1, 'com.ren.admin.controller.NoticeController.addNotice()', 'POST', 1, 'admin', '', '/notice/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747971113,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747969313,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1747998113,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747885398,\"loginDateStr\":\"2025-05-22 11:43:18\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747885398,\"updateTimeStr\":\"2025-05-22 11:43:18\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747969324,\"isClose\":0,\"noticeContent\":\"123\",\"noticeId\":11,\"noticeTitle\":\"123\",\"noticeType\":1,\"remark\":\"123\"}]', '', 0, 'Invalid Addresses: null', NULL, 43700);
INSERT INTO `sys_oper_log` VALUES (114, '通知公告模块', 3, 'com.ren.admin.controller.NoticeController.noticeDelete()', 'DELETE', 1, 'admin', '', '/notice/delete', '127.0.0.1', '内网IP', '[{\"noticeId\":\"11\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 9);
INSERT INTO `sys_oper_log` VALUES (115, '通知公告模块', 1, 'com.ren.admin.controller.NoticeController.addNotice()', 'POST', 1, 'admin', '', '/notice/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747971456,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747969313,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1747998113,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747885398,\"loginDateStr\":\"2025-05-22 11:43:18\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747885398,\"updateTimeStr\":\"2025-05-22 11:43:18\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747969664,\"isClose\":0,\"noticeContent\":\"123\",\"noticeId\":12,\"noticeTitle\":\"123\",\"noticeType\":1,\"remark\":\"312\"}]', '', 0, 'Invalid Addresses: null', NULL, 19152);
INSERT INTO `sys_oper_log` VALUES (116, '通知公告模块', 3, 'com.ren.admin.controller.NoticeController.noticeDelete()', 'DELETE', 1, 'admin', '', '/notice/delete', '127.0.0.1', '内网IP', '[{\"noticeId\":\"12\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 11);
INSERT INTO `sys_oper_log` VALUES (117, '通知公告模块', 1, 'com.ren.admin.controller.NoticeController.addNotice()', 'POST', 1, 'admin', '', '/notice/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747971747,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747969313,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1747998113,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747885398,\"loginDateStr\":\"2025-05-22 11:43:18\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747885398,\"updateTimeStr\":\"2025-05-22 11:43:18\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747969953,\"isClose\":0,\"noticeContent\":\"123\",\"noticeId\":13,\"noticeTitle\":\"123\",\"noticeType\":1,\"remark\":\"312\"}]', '', 0, 'Invalid Addresses: null', NULL, 105682);
INSERT INTO `sys_oper_log` VALUES (118, '通知公告模块', 3, 'com.ren.admin.controller.NoticeController.noticeDelete()', 'DELETE', 1, 'admin', '', '/notice/delete', '127.0.0.1', '内网IP', '[{\"noticeId\":\"13\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 10);
INSERT INTO `sys_oper_log` VALUES (119, '通知公告模块', 1, 'com.ren.admin.controller.NoticeController.addNotice()', 'POST', 1, 'admin', '', '/notice/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747972063,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747969313,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1747998113,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747885398,\"loginDateStr\":\"2025-05-22 11:43:18\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747885398,\"updateTimeStr\":\"2025-05-22 11:43:18\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747970271,\"isClose\":0,\"noticeContent\":\"123\",\"noticeId\":14,\"noticeTitle\":\"123\",\"noticeType\":1,\"remark\":\"123\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 8727);
INSERT INTO `sys_oper_log` VALUES (120, '通知公告模块', 3, 'com.ren.admin.controller.NoticeController.noticeDelete()', 'DELETE', 1, 'admin', '', '/notice/delete', '127.0.0.1', '内网IP', '[{\"noticeId\":\"14\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 7);
INSERT INTO `sys_oper_log` VALUES (121, '通知公告模块', 1, 'com.ren.admin.controller.NoticeController.addNotice()', 'POST', 1, 'admin', '', '/notice/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747972494,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747969313,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1747998113,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747885398,\"loginDateStr\":\"2025-05-22 11:43:18\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747885398,\"updateTimeStr\":\"2025-05-22 11:43:18\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747970701,\"isClose\":0,\"noticeContent\":\"123\",\"noticeId\":15,\"noticeTitle\":\"231\",\"noticeType\":1,\"remark\":\"123\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 10107);
INSERT INTO `sys_oper_log` VALUES (122, '通知公告模块', 3, 'com.ren.admin.controller.NoticeController.noticeDelete()', 'DELETE', 1, 'admin', '', '/notice/delete', '127.0.0.1', '内网IP', '[{\"noticeId\":\"15\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 15);
INSERT INTO `sys_oper_log` VALUES (123, '用户模块', 1, 'com.ren.admin.controller.UserController.addUser()', 'POST', 1, 'admin', '', '/user/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747979322,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747977536,\"deptId\":100,\"email\":\"123@qq.com\",\"isDel\":0,\"nickname\":\"123\",\"password\":\"$2a$10$xtRWV039WD0xkINYR1Etu.eNiETZNo2zqu8.NU.veJkZV9YfLvm6O\",\"phonenumber\":\"13111111111\"', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 9984);
INSERT INTO `sys_oper_log` VALUES (124, '字典模块', 1, 'com.ren.admin.controller.DictTypeController.addDictType()', 'POST', 1, 'admin', '', '/dictType/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747981363,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747979661,\"dictCode\":\"sys-user-usertype\",\"dictName\":\"用户类型\",\"dictTypeId\":0,\"isStop\":0,\"remark\":\"用户类型\"}]', '', 0, '\r\n### Error updating database.  Cause: java.sql.SQLException: Incorrect integer value: \'sys-user-usertype\' for column \'is_stop\' at row 1\r\n### The error may exist in file [C:\\Users\\12296\\Desktop\\work\\ideaCode\\Ren\\Ren-Demo\\ren-system\\target\\classes\\mapper\\DictTypeMapper.xml]\r\n### The error may involve com.ren.system.mapper.DictTypeMapper.insertDictType-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into sys_dict_type(                    dict_name,           dict_code,           is_stop,           remark,          create_by,         create_time         )values(                    ?,           ?,           ?,           ?,          ?,         ?         )\r\n### Cause: java.sql.SQLException: Incorrect integer value: \'sys-user-usertype\' for column \'is_stop\' at row 1\n; uncategorized SQLException; SQL state [HY000]; error code [1366]; Incorrect integer value: \'sys-user-usertype\' for column \'is_stop\' at row 1', NULL, 161);
INSERT INTO `sys_oper_log` VALUES (125, '字典模块', 1, 'com.ren.admin.controller.DictTypeController.addDictType()', 'POST', 1, 'admin', '', '/dictType/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747981363,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747979739,\"dictCode\":\"sys-user-usertype\",\"dictName\":\"用户类型\",\"dictTypeId\":0,\"isStop\":0,\"remark\":\"用户类型\"}]', '', 0, '\r\n### Error updating database.  Cause: java.sql.SQLException: Incorrect integer value: \'sys-user-usertype\' for column \'is_stop\' at row 1\r\n### The error may exist in file [C:\\Users\\12296\\Desktop\\work\\ideaCode\\Ren\\Ren-Demo\\ren-system\\target\\classes\\mapper\\DictTypeMapper.xml]\r\n### The error may involve com.ren.system.mapper.DictTypeMapper.insertDictType-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into sys_dict_type(                    dict_name,           dict_code,           is_stop,           remark,          create_by,         create_time         )values(                    ?,           ?,           ?,           ?,          ?,         ?         )\r\n### Cause: java.sql.SQLException: Incorrect integer value: \'sys-user-usertype\' for column \'is_stop\' at row 1\n; uncategorized SQLException; SQL state [HY000]; error code [1366]; Incorrect integer value: \'sys-user-usertype\' for column \'is_stop\' at row 1', NULL, 54620);
INSERT INTO `sys_oper_log` VALUES (126, '字典模块', 1, 'com.ren.admin.controller.DictTypeController.addDictType()', 'POST', 1, 'admin', '', '/dictType/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747981563,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747979794,\"dictCode\":\"sys-user-usertype\",\"dictName\":\"用户类型\",\"dictTypeId\":101,\"isStop\":0,\"remark\":\"用户类型\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 2512);
INSERT INTO `sys_oper_log` VALUES (127, '字典模块', 1, 'com.ren.admin.controller.DictDataController.addDictData()', 'POST', 1, 'admin', '', '/dictData/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747981758,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747979985,\"cssClass\":\"1\",\"dictDataId\":101,\"dictLabel\":\"系统用户\",\"dictSort\":1,\"dictType\":\"用户类型\",\"dictValue\":\"system\",\"isDefault\":0,\"listClass\":\"1\",\"remark\":\"系统用户\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 7);
INSERT INTO `sys_oper_log` VALUES (128, '字典模块', 2, 'com.ren.admin.controller.DictDataController.modifyDictData()', 'POST', 1, 'admin', '', '/dictData/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747981920,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"cssClass\":\"1\",\"dictDataId\":101,\"dictLabel\":\"系统用户\",\"dictSort\":1,\"dictType\":\"用户类型\",\"dictValue\":\"system\",\"isDefault\":1,\"listClass\":\"1\",\"remark\":\"系统用户\",\"updateBy\":\"admin\",\"updateTime\":1747980210}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 18);
INSERT INTO `sys_oper_log` VALUES (129, '字典模块', 2, 'com.ren.admin.controller.DictDataController.modifyDictData()', 'POST', 1, 'admin', '', '/dictData/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747982868,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"cssClass\":\"1\",\"dictDataId\":101,\"dictLabel\":\"系统用户\",\"dictSort\":1,\"dictType\":\"sys-user-usertype\",\"dictValue\":\"00\",\"isDefault\":1,\"listClass\":\"1\",\"remark\":\"系统用户\",\"updateBy\":\"admin\",\"updateTime\":1747981145}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 18);
INSERT INTO `sys_oper_log` VALUES (130, '用户模块', 1, 'com.ren.admin.controller.UserController.addUser()', 'POST', 1, 'admin', '', '/user/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747983051,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747981290,\"deptId\":100,\"email\":\"123@qq.com\",\"isDel\":0,\"nickname\":\"123\",\"password\":\"$2a$10$32QPli1Vnx7mWbur6YZnmOh9ZINtVxxF5B0UuCvrGZxAU3QwHyVp6\",\"phonenumber\":\"13111111111\"', '', 0, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'user_type\' at row 1\r\n### The error may exist in file [C:\\Users\\12296\\Desktop\\work\\ideaCode\\Ren\\Ren-Demo\\ren-system\\target\\classes\\mapper\\UserMapper.xml]\r\n### The error may involve com.ren.system.mapper.UserMapper.insertUser-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into sys_user(                    username,           nickname,           password,           is_del,           dept_id,           email,           user_type,           phonenumber,           sex,                     remark,          create_by,         create_time         )values(                    ?,           ?,           ?,           ?,           ?,           ?,           ?,           ?,           ?,                     ?,          ?,         ?         )\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'user_type\' at row 1\n; Data truncation: Data too long for column \'user_type\' at row 1', NULL, 25199);
INSERT INTO `sys_oper_log` VALUES (131, '用户模块', 1, 'com.ren.admin.controller.UserController.addUser()', 'POST', 1, 'admin', '', '/user/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747983051,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"createBy\":\"admin\",\"createTime\":1747981310,\"deptId\":100,\"email\":\"123@qq.com\",\"isDel\":0,\"nickname\":\"123\",\"password\":\"$2a$10$U6CyDZab1iZIHtNf.2B4F.YAUqXORqf240t3eJACExJatg35dJppK\",\"phonenumber\":\"13111111111\"', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 75);
INSERT INTO `sys_oper_log` VALUES (132, '用户模块', 2, 'com.ren.admin.controller.UserController.modifyUser()', 'POST', 1, 'admin', '', '/user/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747985171,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"deptId\":103,\"email\":\"ren@yeah.com\",\"nickname\":\"张三\",\"phonenumber\":\"18422222222\",\"postIdArr\":[1,2],\"remark\":\"张三\",\"roleIdArr\":[2,109],\"roleList\":[],\"sex\":1,\"updateBy\":\"admin\",\"updateTime\":1747983381,\"userId\"', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 32);
INSERT INTO `sys_oper_log` VALUES (133, '用户模块', 2, 'com.ren.admin.controller.UserController.modifyUser()', 'POST', 1, 'admin', '', '/user/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1747985171,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1747972261,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1748001061,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747969313,\"loginDateStr\":\"2025-05-23 11:01:53\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747969313,\"updateTimeStr\":\"2025-05-23 11:01:53\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"deptId\":103,\"email\":\"ren@yeah.com\",\"nickname\":\"张三\",\"phonenumber\":\"18422222222\",\"postIdArr\":[2],\"remark\":\"张三\",\"roleIdArr\":[2,109],\"roleList\":[],\"sex\":1,\"updateBy\":\"admin\",\"updateTime\":1747983388,\"userId\":5', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 17);
INSERT INTO `sys_oper_log` VALUES (134, '菜单模块', 1, 'com.ren.admin.controller.system.MenuController.addMenu()', 'POST', 1, 'admin', '', '/menu/add', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1750400657,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1750398725,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1750427525,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747972261,\"loginDateStr\":\"2025-05-23 11:51:01\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747972261,\"updateTimeStr\":\"2025-05-23 11:51:01\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"children\":[],\"component\":\"\",\"createBy\":\"admin\",\"createTime\":1750398922,\"icon\":\"i-ep-basketball\",\"isCache\":0,\"isFrame\":0,\"isStop\":0,\"isVisible\":0,\"menuId\":2002,\"menuName\":\"系统任务\",\"menuType\":\"M\",\"orderNum\":1', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 14);
INSERT INTO `sys_oper_log` VALUES (135, '菜单模块', 2, 'com.ren.admin.controller.system.MenuController.modifyMenu()', 'POST', 1, 'admin', '', '/menu/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1750400725,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1750398725,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1750427525,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747972261,\"loginDateStr\":\"2025-05-23 11:51:01\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747972261,\"updateTimeStr\":\"2025-05-23 11:51:01\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"children\":[],\"component\":\"system/task/Index\",\"icon\":\"i-ep-dishdot\",\"isCache\":0,\"isFrame\":1,\"isStop\":0,\"isVisible\":0,\"menuId\":110,\"menuName\":\"定时任务\",\"menuType\":\"C\",\"orderNum\":20,\"parentId\":2002,\"path\":\"task', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 6);
INSERT INTO `sys_oper_log` VALUES (136, '菜单模块', 3, 'com.ren.admin.controller.system.MenuController.menuDelete()', 'DELETE', 1, 'admin', '', '/menu/delete', '127.0.0.1', '内网IP', '[{\"menuId\":\"111\"}]', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 8);
INSERT INTO `sys_oper_log` VALUES (137, '菜单模块', 2, 'com.ren.admin.controller.system.MenuController.modifyMenu()', 'POST', 1, 'admin', '', '/menu/modify', '127.0.0.1', '内网IP', '[{\"authorities\":[{\"authority\":\"system:post:list\"},{\"authority\":\"monitor:druid:list\"},{\"authority\":\"system:user:list\"},{\"authority\":\"system:notice:list\"},{\"authority\":\"system:menu:list\"},{\"authority\":\"system:config:list\"},{\"authority\":\"system:role:list\"},{\"authority\":\"monitor:cache:list\"},{\"authority\":\"monitor:job:list\"},{\"authority\":\"monitor:online:list\"},{\"authority\":\"system:dept:list\"},{\"authority\":\"tool:gen:list\"},{\"authority\":\"system:logininfor:list\"},{\"authority\":\"system:dict:list\"},{\"authority\":\"system:operlog:list\"},{\"authority\":\"monitor:server:list\"},{\"authority\":\"tool:build:list\"},{\"authority\":\"tool:swagger:list\"}],\"browser\":\"MSEdge\",\"expireTime\":1750400823,\"ipaddr\":\"127.0.0.1\",\"loginLocation\":\"内网IP\",\"loginTime\":1750398725,\"os\":\"Windows 10 or Windows Server 2016\",\"permissions\":[\"system:post:list\",\"monitor:druid:list\",\"system:user:list\",\"system:notice:list\",\"system:menu:list\",\"system:config:list\",\"system:role:list\",\"monitor:cache:list\",\"monitor:job:list\",\"monitor:online:list\",\"system:dept:list\",\"tool:gen:list\",\"system:logininfor:list\",\"system:dict:list\",\"system:operlog:list\",\"monitor:server:list\",\"tool:build:list\",\"tool:swagger:list\"],\"refreshTokenExpireTime\":1750427525,\"user\":{\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":1745934956,\"createTimeStr\":\"2025-04-29 21:55:56\",\"dept\":{\"children\":[],\"deptId\":103},\"deptId\":103,\"email\":\"1229610498@qq.com\",\"isDel\":0,\"loginDate\":1747972261,\"loginDateStr\":\"2025-05-23 11:51:01\",\"loginIp\":\"127.0.0.1\",\"nickname\":\"超级管理员\",\"password\":\"{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha\",\"phonenumber\":\"18411111111\",\"remark\":\"超级管理员\",\"roleList\":[],\"sex\":0,\"updateBy\":\"admin\",\"updateTime\":1747972261,\"updateTimeStr\":\"2025-05-23 11:51:01\",\"userId\":1,\"userType\":\"00\",\"username\":\"admin\"},\"userId\":1,\"username\":\"admin\"},{\"children\":[],\"component\":\"monitor/cache/List\",\"icon\":\"i-ep-locationinformation\",\"isCache\":0,\"isFrame\":1,\"isStop\":0,\"isVisible\":0,\"menuId\":114,\"menuName\":\"缓存列表\",\"menuType\":\"C\",\"orderNum\":60,\"parentId\":2,\"p', '{\"msg\":\"操作成功\",\"code\":200}', 1, '', NULL, 5);

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, 0, 'admin', 1747626140, 'admin', 1747636454, '董事长');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, 0, 'admin', 1747626140, 'admin', 1747636458, '项目经理');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, 0, 'admin', 1747626140, 'admin', 1747636462, '人力资源');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, 0, 'admin', 1747626140, 'admin', 1747636466, '普通员工');

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
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 0, 'admin', 1, 0, 'admin', 1747626140, '', NULL, '超级管理员', 1);
INSERT INTO `sys_role` VALUES (2, '普通角色', 0, 'common', 3, 0, 'admin', 1747626140, 'admin', 1747102588, '普通角色', 2);
INSERT INTO `sys_role` VALUES (109, '管理员', 0, 'administrators', 2, 0, 'admin', 1747626140, 'admin', 1747102631, '管理员', 1);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超级管理员', '{bcrypt}$2a$10$rbt2eZHv6R0MTBDGTnE87.3mKDgP3Zfi.ecvE/1J4mYItQJ6w2Dha', 0, 103, '00', '1229610498@qq.com', '18411111111', 0, '', '127.0.0.1', 1750398726, 'admin', 1745934956, 'admin', 1750398726, '超级管理员');
INSERT INTO `sys_user` VALUES (5, 'zhangsan', '张三', '{bcrypt}$2a$10$vJGLDCIgJz03bOTLjt98Eueg24A6cCxzYC9Ky8pPugdUqbky31nou', 0, 103, '00', 'ren@yeah.com', '18422222222', 1, '', '127.0.0.1', 1746351509, 'admin', 1746347572, 'admin', 1747983388, '张三');
INSERT INTO `sys_user` VALUES (6, 'wangwu', '王五', '{bcrypt}$2a$10$2DNzMqN2J/khioiX9fgHIuXUR0inkKk.aNyc8TGCZulRFxyNKERim', 0, 109, '00', 'ren@yeah.com', '18155555555', 2, '', '', NULL, 'admin', 1746436640, 'admin', 1747054083, '王五');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (5, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (5, 2);
INSERT INTO `sys_user_role` VALUES (5, 109);
INSERT INTO `sys_user_role` VALUES (6, 2);
INSERT INTO `sys_user_role` VALUES (6, 109);

SET FOREIGN_KEY_CHECKS = 1;
