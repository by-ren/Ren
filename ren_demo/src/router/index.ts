// 第一步：引入createRouter
import {createRouter,createWebHistory, type RouteRecordRaw} from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { storeToRefs } from 'pinia'

// 系统整个流程为：用户访问页面 -> 触发路由守卫 -> 判断路由是否需要认证，如果不需要认证，请求放行 -> 如果需要认证，判断用户信息是否存在 -> 如果存在，请求放行 -> 如果不存在，带着accessToken去获取用户信息
// -> 如果accessToken存在且合法，则直接获取到用户信息，请求放行 -> 如果accessToken不存在或者不合法，从localStorage获取refreshToken -> 如果存在，则去刷新token，刷新成功，重新处理原请求，处理成功，请求放行
// -> refreshToken如果不存在，进入登陆页面
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
        path: 'index',
        name: 'Index',
        component: () => import('@/views/Index.vue'),
        meta: { requiresAuth: true } // 需要认证
      }
    ]
  },
  { 
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
  }
]

// 创建路由器
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();
  
  // 检查路由是否存在（包含动态路由）
  const routeExists = router.hasRoute(to.name!);
  // 递归判断路由是否需要权限认证
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  // 放行无需认证的路由
  if (routeExists && !requiresAuth) {
    next();
    return;
  }

  //认证状态
  const { isLogin, dynamicRoutes } = storeToRefs(authStore);

  try {
    //未登录，跳转到登录页
    if(!isLogin.value){
        //尝试自动登录
        let result = await authStore.autoLogin();
        if(result.code != 200){
          // 自动登录失败，清除无效 token 并跳转登录页
          authStore.logout();
          return;
        }
    }

    if (dynamicRoutes.value.length === 0) {//已登录，并且路由数组为空，则调用获取用户信息接口
      //调用获取用户、角色、菜单、路由、按钮权限等相关配置
      await authStore.getUserInfo();
      // 执行路由注入
      await addDynamicRoutes();
      // 通过请求，不过这里需要注意，因为上方进行了路由动态注入，所以这里需要按照新的路由表进行请求，所以不能直接next()
      // 解析当前的请求路径，重新生成一个包含路由匹配信息的对象
      const retryMatch = router.resolve(to.path);
      // 检查重新解析后的路由对象是否匹配到了有效的路由配置。
      if (retryMatch.matched.length > 0) {
        // 标识匹配到了，进行跳转
        next(retryMatch);
        return;
      }
    }else{//已登录，并且路由数组不为空，表示已经加载过动态路由，直接通过请求即可
      // 通过请求
      next();
    }
  } catch (error) {
    // 清除无效 token 并跳转登录页
    authStore.logout();
  }
})

// 全局错误处理
router.onError((error) => {
  if (error.message.includes('No match')) {
    router.push('/404');
  }
});

// 动态路由注入方法
const addDynamicRoutes = () => {
  // 获取状态管理中的内容
  const authStore = useAuthStore();

  // 预加载所有视图组件
  const modules = import.meta.glob('/src/views/**/*.vue');

  // 遍历给MainLayout添加子路由
  authStore.dynamicRoutes.forEach(route => {
    // 添加前先检查是否已存在同名路由
    if (!router.hasRoute(route.name)) {
      router.addRoute('MainLayout', { 
        path: route.path,
        name: route.name,
        component: modules[`/src/views/${route.component}.vue`], // 直接匹配预加载的组件
        meta: {
          requiresAuth: true,
          roles: route.meta?.roles || []
        }
      });
    }
  })

  // 修改通配符路由添加逻辑：
  router.addRoute({
    path: '/:pathMatch(.*)*',
    name: 'NotFoundCatchAll',
    redirect: '/404'
  });

}

export default router;