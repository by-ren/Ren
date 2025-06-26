import { createApp } from "vue";
import { createPinia } from "pinia";
// 引入app组件
import App from "./App.vue";
// 引入路由组件
import router from "./router";
//引入axios过滤器
import { service, setupInterceptors } from "@/axios/axios";

//引入SvgIcon字体图标
//import SvgIcon from '@/components/SvgIcon/index.vue';

// 配合vite.config.ts中的createSvgIconsPlugin，用于生成雪碧图（注意，必须入口文件main.ts引入，不能在vite.config.ts引入，因为他以来了node.js）
import "virtual:svg-icons-register";
// el-icon
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
// 引入全局 CSS
import "@/styles/global.scss"; // 或 import './styles/global.scss'

//ElementPlus全局中文配置
import ElementPlus from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";

// 创建app
const app = createApp(App);
// 创建pinia
const pinia = createPinia();

// 引入pinia
app.use(pinia);
// 引入router
app.use(router);

// ElementPlus全局中文配置
app.use(ElementPlus, {
	locale: zhCn,
});

// 注册自定义 SVG 组件
// ​第一个参数 'SvgIcon'，表示 ​组件在模板中使用的标签名​（字符串类型），在模板中通过 <svg-icon> 或 <SvgIcon> 调用（Vue 支持两种写法，但需注意命名规范），推荐使用 kebab-case（短横线分隔）如 svg-icon，但 PascalCase（大驼峰）如 SvgIcon 也兼容
// 第二个参数 SvgIcon，表示 ​具体的组件对象​（变量名），通常是导入的 .vue 文件或 JSX 组件
// 因为项目中没有用到针对这个自定义组件的动态绑定，只正常用到了模板中使用，并且我已经在vite.config.ts中配置了按需导入自定义组件，所以这里就无需全局注册SvgIcon了
// app.component('SvgIcon', SvgIcon);

// 循环​批量注册 Element Plus 图标，注册为i-ep-xxx 格式
// 如果项目中没有使用动态绑定组件，那么main.ts中的全局注册和vite.config.ts中的按需导入，只需要任选其一即可（vite.config.ts中的按需导入优先，性能更好）
// 如果项目中使用到了动态绑定组件，那么这里必须配置全局注册，vite.config.ts中就无需配置按需导入，当然配置了也不会出错
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(`i-ep-${key.toLowerCase()}`, component);
}

//挂载App
app.mount("#app");

//在Pinia初始化完成之后创建axios拦截器
setupInterceptors();
