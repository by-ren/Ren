import { createApp } from 'vue'
import { createPinia } from 'pinia'

// 引入app组件
import App from './App.vue'
// 引入路由组件
import router from './router'

// 创建app
const app = createApp(App)
// 创建pinia
const pinia = createPinia()

// 引入pinia
app.use(pinia)
// 引入router
app.use(router)

app.mount('#app')