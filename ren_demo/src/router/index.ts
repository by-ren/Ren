// 第一步：引入createRouter
import {createRouter,createWebHistory} from 'vue-router'

// 第二步：创建路由器
const router = createRouter({
  history:createWebHistory(),
  routes:[ //一个一个的路由规则
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue'),
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
    }
  ]
})

// 暴露出去router
export default router