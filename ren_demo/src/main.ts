import { createApp } from 'vue'
import { createPinia } from 'pinia'

// 引入app组件
import App from './App.vue'
// 引入路由组件
import router from './router'
import { service, setupInterceptors } from '@/utils/axios';
import SvgIcon from '@/components/SvgIcon/index.vue';
import 'virtual:svg-icons-register'; // 自动注入SVG符号表[2,5](@ref)

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

app.mount('#app')

//在Pinia初始化完成之后创建axios拦截器
setupInterceptors();