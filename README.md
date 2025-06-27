# 任后台管理系统

![Java](https://img.shields.io/badge/Java-17%2B-blue) ![License](https://img.shields.io/badge/License-Apache_2.0-green)

> 基于SpringBoot+Vue3前后端分离的Java快速开发框架

## 🚀 系统简介

任是一套全部开源的快速开发平台，现阶段所有功能全部免费开放给个人开发者与企业学习和使用

- 前端采用Vue3+TypeScript+ElementPlus
- 后端采用Spring Boot、Spring Security、Redis & Jwt、Quartz。
- 使用双Token进行登录验证，实现Token无感续期，Token黑名单，强退用户等功能
- 动态加载权限菜单与菜单路由

## 🖥️ 内置功能

已完成：

- 系统管理
  - 用户管理：用户是该系统的核心功能，主要用于登录与操作系统
  - 角色管理：角色菜单权限分配，同时可以设置角色可查看的数据范围
  - 菜单管理：配置系统菜单，操作权限，按钮权限等
  - 部门管理：配置系统部门结构，树形结构展现数据
  - 岗位管理：配置用户在组织中所担任的职务
  - 字典管理：对系统中常用的一些相对固定的数据进行维护（如常量等，基本不会变动的数据尽量维护在代码常量类中）
  - 参数设置：对系统中常用参数进行动态配置
  - 通知公告：系统有变动时进行维护，如新增公告，会发邮件通知所有用户
  - 日志管理
    - 操作日志：查看用户在本系统操作的所有记录
    - 登录日志：查看本系统用户的登陆记录
- 系统监控
  - 在线用户：查看本系统已经登陆的所有用户，超级管理员在该界面可以对用户账号进行强退
  - 服务监控：查看服务器CPU、内存、Java虚拟机以及服务器本身详细信息
  - 缓存列表：查看Redis中所有数据内容
  - 缓存监控：对Redis的缓存情况进行监控
- 系统任务
  - 定时任务：对系统中需要定时执行的任务进行配置，如定时取消订单
- 组件示例
  - 图片上传组件：包含OSS云上传与本地上传


即将完成：

- 系统工具
  - 表单构建：通过拖拉拽的方式，快速进行表单的排版与构建，极大节省了前端页面的编写
  - 代码生成：快速完成从实体类、接口、Mapper层的代码生成，节省了二次开发人员的大量重复无意义工作
  - 系统接口：使用Swagger，快速查看系统中所涉及到的所有接口，可对接口进行调试，与接口文档生成

未来规划：

1. 系统基础功能完成后将逐步加入支付宝支付，微信支付等等各种支付功能的傻瓜式开发，同时添加各步骤操作手册，旨在让二次开发人员只通过简单的配置与修改即可完成一些复杂功能的开发，节省大量查询资料与配置时间
2. 逐步加入工业上各种协议的对接模块，如MQTT、MODBUS等协议对接，同样配备傻瓜式的各步骤操作手册，让二次开发人员只需要按照操作手册进行简单配置即可完成复杂协议对接
3. 以上模块完成开发后不会内置于项目中，而会作为独立可插拔模块，独立于项目之外，尽量提高原始项目的简洁性，让不需要的用户可以更简单的使用系统，同时让需要的用户可以通过简单的导入即可使用
4. 该项目诞生的初衷就是让不论初级还是高级开发者都可以顺畅的使用，基于此目标，该项目中添加了非常多的注释，复杂功能甚至达到了一行注释一行代码，帮助大家快速的理解项目

## 🗂️ 项目结构

**后台**

```
Ren-Demo/
├── ren-admin			    # 项目启动的主启动类与控制层所处模块
├── ren-business			# 业务总模块
│   ├── ren-example			# 业务示例模块（本模块无实际功能，仅作为代码规范示例使用，方便二次开发者参照，可删除）
│   ├── ren-localstorage	# 本地上传图片模块
├── ren-common			    # 通用模块（主要用于存放各个模块通用内容，如常量类，公共控制器，通用实体、VO、BO类，注解，配置文件，工具类）
├── ren-feature			    # 可插拔功能模块
│   ├── ren-cloudstorage	# 云上传图片模块（注意：该模块虽可独立使用，配套上传组件与数据库表分别为/ImageUpload/index.vue、sys_image_log）
├── ren-framework			# 核心模块（主要用于存放系统核心业务，如AOP切面，拦截器，监听器，通用接口以及权限认证等内容）
├── ren-monitor				# 监控模块，存放监控模块所有内容
├── ren-quartz				# 定时任务，存放定时任务所有内容
└── ren-system				# 系统模块，存放系统模块所有内容 
```

**前台**

```
ren_demo/
├── build                        # 构建相关  
├── bin                          # 执行脚本
├── public                       # 公共文件
│   └── favicon.ico              # favicon图标
├── src/
│   ├── api			    	    # 存放所有后台请求接口
│   ├── assets				    # 存放前端静态资源
│   ├── axios				    # 存放异步请求相关内容
│   ├── components				# 自定义可复用的Vue组件
│   ├── hooks				    # 主要用于存放VUE3的组合式API的
│   ├── layout				    # 主要用于存放布局文件内容
│   ├── router				    # 主要用于存放路由相关内容
│   ├── stores				    # 主要用于存放Pinia本地数据相关内容
│   ├── styles				    # 主要用于存放前端样式文件
│   ├── types				    # 主要用于存放TS类文件
│   ├── utils				    # 主要用于存放前端所需工具类
│   ├── views				    # 页面文件主文件夹
│   ├── App.vue					# 入口页面
│   └── main.ts					# 入口 加载组件 初始化等
├── .env.development			# 开发环境配置
├── .env.production				# 生产环境配置
├── .env.staging				# 测试环境配置
├── .gitattributes				# 定义Git如何处理特定文件（如换行符）
├── .gitignore					# git 忽略项
├── .prettierrc					# 代码格式化工具配置文件
├── auto-imports.d.ts		     # 自动导入的组件/API的类型声明文件
├── components.d.ts			     # 全局组件的TypeScript类型声明
├── index.html					# 应用入口,唯一HTML文件
├── package.json				# 项目身份证,依赖/脚本/元数据
│   ├── .editorconfig		     # 统一团队代码风格（缩进/字符集等）
│   ├── eslint.config.ts		 # TypeScript版的ESLint代码检查规则    	    
│   └── package-lock.json		 # 锁定依赖版本，确保安装一致性	    
├── README.md					# 说明文档
├── tsconfig.json			     # TypeScript总配置
│   ├── env.d.ts			     # 环境变量类型声明
│   ├── tsconfig.app.json	      # 应用代码编译配置
│   └── tsconfig.node.json		  # Node环境（如vite.config）类型配置
└── vite.config.js				 # 项目心脏,构建/开发服务器配置
```

## ⚡ 5分钟快速体验

```bash
# 后端启动
git clone https://gitee.com/huimouyixiaojifeigoutiao/Ren.git
#或
git clone https://github.com/by-ren/Ren.git
#注意根据自己下载的项目路径，调整命令
cd Ren-Demo/ren-admin
mvn spring-boot:run

# 前端启动
cd ren-ui
npm install
npm run dev
```

> [!TIP]
>
> 默认账号：admin
> 默认密码：111111
> 访问地址：http://localhost:5173

## ✨ 作者寄语

> 这个项目始于闲暇时的灵感火花，却在开发过程中逐渐成为承载理想的载体。  
> 作为独立开发者，我对未来的规划越发清晰，但开发进度难免受限于个人精力。  
> **请相信：所有规划的功能都会如约而至，只是需要您多一份耐心等待。**  

## 🚀 诚邀同行者  

**如果您：**  

- 渴望打造一款与众不同的开源框架  
- 愿与志同道合者并肩作战  
- 被Java/Vue全栈开发所吸引  

**欢迎通过邮箱联系我：**  
📮 wy18434294023@163.com  
（每封邮件必复！若响应热烈，我们将建立专属开发者社群）  

**深夜改bug时最暖的光，  
是看到"Star"数又新增一颗✨**  

## 📝 项目截图

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/87988c8e/32562f29.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/17c8cb9b/b0cca69c.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/162acef3/33c5502a.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/6429f9bd/36f74a56.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/914e1852/cfd10474.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/dd471baa/4fb98d02.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/f775bdea/9407cdad.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/eb3fcba7/032b2832.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/be3b32e6/0ae2b5da.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/cc760e81/fd52a2ef.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/7f4a6fd9/ba9bd11e.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/4f181119/d2b844cb.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/ee4ef7f9/f1253777.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/ff892d3d/8c1050df.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/74b5d97b/c1705393.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/23bfbece/bf2e74f3.png)

![License](https://pic.yupoo.com/huimouyixiaojifeigoutiao/57803ac1/1bf56f47.png)

## 📜 许可证

本项目采用 [Apache License 2.0](LICENSE) 开源协议，允许：  
- ✅ 商业使用  
- ✅ 修改源码  
- ✅ 专利授权  
- ❗ **但必须保留原始许可声明**（见源码文件头）  

---

📫 **反馈联系**: wy18434294023@163.com | [讨论区](https://github.com/by-ren/Ren/issues)

