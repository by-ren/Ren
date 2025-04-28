import { createApp } from 'vue'
import { createPinia } from 'pinia'

// 引入app组件
import App from './App.vue'
// 引入路由组件
import router from './router'
//引入axios过滤器
import { service, setupInterceptors } from '@/utils/axios';
//引入SvgIcon字体图标
import SvgIcon from '@/components/SvgIcon/index.vue';
// 自动注入SVG符号表[2,5](@ref)
import 'virtual:svg-icons-register';
// el-icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 引入全局 CSS
import '@/styles/global.css' // 或 import './styles/global.css'

// 创建app
const app = createApp(App)
// 创建pinia
const pinia = createPinia()

// 引入pinia
app.use(pinia)
// 引入router
app.use(router)
//引入SvgIcon
app.component('SvgIcon', SvgIcon);

//导入所有Element Plus图标并全局注册
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(`i-ep-${key.toLowerCase()}`, component) // 全局注册为 i-ep-house 格式
}

//挂载App
app.mount('#app')

//在Pinia初始化完成之后创建axios拦截器
setupInterceptors();