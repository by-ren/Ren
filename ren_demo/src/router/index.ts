// 第一步：引入createRouter
import {createRouter,createWebHistory, type RouteRecordRaw} from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { storeToRefs } from 'pinia'

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false } // 明确标记不需要认证
  },
  {
    path: '/',
    name: 'MainLayout',
    component: () => import('@/views/MainLayout.vue'),
    children: [
      {
        path: '', // 空路径作为默认
        name: 'MainLayoutDefault', // 添加显式名称
        redirect: { name: 'Index' } // 重定向到首页
      },
      {
        path: '/index',
        name: 'Index',
        component: () => import('@/views/Index.vue'),
        meta: { requiresAuth: true } // 需要认证
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('@/views/user/Index.vue'),
        meta: { requiresAuth: true } // 需要认证
      }
    ]
  }
]

// 创建路由器
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // 1. 放行无需认证的路由
  if (!to.meta.requiresAuth) {
    next()
    return
  }

  //认证状态
  const { isAuthenticated } = storeToRefs(authStore)

  // 2. 已认证直接放行
  if (isAuthenticated.value) {
    next()
    return
  }

  // 3. 尝试自动登录（处理页面刷新场景）
  try {
    // 尝试获取用户信息（会更新 isAuthenticated）
    await authStore.getUserInfo()
    
    if (isAuthenticated.value) {
      next() // 认证成功继续导航
    } else {
      // 携带重定向路径跳转登录页
      next({
        path: '/login'
      })
    }
  } catch (error) {
    // 清除无效 token 并跳转登录页
    authStore.logout()
    next('/login')
  }
})

export default router;