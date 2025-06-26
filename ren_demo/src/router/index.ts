// 第一步：引入createRouter
import { createRouter, createWebHistory, type RouteRecordRaw } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { storeToRefs } from "pinia";
import type { DynamicRoute } from "@/types/DynamicRoute";

// 系统整个流程为：用户访问页面 -> 触发路由守卫 -> 判断路由是否需要认证，如果不需要认证，请求放行 -> 如果需要认证，判断用户信息是否存在 -> 如果存在，请求放行 -> 如果不存在，带着accessToken去获取用户信息
// -> 如果accessToken存在且合法，则直接获取到用户信息，请求放行 -> 如果accessToken不存在或者不合法，从localStorage获取refreshToken -> 如果存在，则去刷新token，刷新成功，重新处理原请求，处理成功，请求放行
// -> refreshToken如果不存在，进入登陆页面
// 路由配置
const routes: RouteRecordRaw[] = [
	{
		path: "/login",
		name: "login",
		component: () => import("@/views/Login.vue"),
		meta: { requiresAuth: false }, // 明确标记不需要认证
	},
	{
		path: "/",
		name: "mainLayout",
		component: () => import("@/layout/MainLayout.vue"),
		children: [
			{
				path: "", // 空路径作为默认
				name: "mainLayoutDefault", // 添加显式名称
				redirect: { name: "index" }, // 重定向到首页
			},
			{
				path: "index",
				name: "index",
				component: () => import("@/views/Index.vue"),
				meta: { requiresAuth: true, roles: [], menuShow: "/index" }, // 需要认证
			},
			{
				path: "/system/dict",
				name: "dict",
				component: () => import("@/views/system/dict/Index.vue"),
				children: [
					{
						path: "", // 空路径作为默认
						name: "dictDefault", // 添加显式名称
						redirect: { name: "dictType" }, // 重定向到字典类型
					},
					{
						path: "/system/dict/dictType",
						name: "dictType",
						component: () => import("@/views/system/dict/DictType.vue"),
						meta: { requiresAuth: true, roles: [], menuShow: "/system/dict" }, // 需要认证
					},
					{
						path: "/system/dict/dictData",
						name: "dictData",
						component: () => import("@/views/system/dict/DictData.vue"),
						meta: { requiresAuth: true, roles: [], menuShow: "/system/dict" }, // 需要认证
					},
				],
				meta: { requiresAuth: true, roles: [], menuShow: "/system/dict" }, // 需要认证
			},
		],
	},
	{
		path: "/404",
		name: "NotFound",
		component: () => import("@/layout/NotFound.vue"),
	},
];

// 创建路由器
const router = createRouter({
	history: createWebHistory(),
	routes,
});

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
	const authStore = useAuthStore();

	// 检查路由是否存在（包含动态路由）
	const routeExists = router.hasRoute(to.name!);
	// 递归判断路由是否需要权限认证
	const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
	// 放行无需认证的路由
	if (routeExists && !requiresAuth) {
		next();
		return;
	}

	//认证状态
	const { isLogin, dynamicRoutes } = storeToRefs(authStore);

	try {
		//未登录，跳转到登录页
		if (!isLogin.value) {
			//尝试自动登录
			let result = await authStore.autoLogin();
			if (result.code != 200) {
				// 自动登录失败，清除无效 token 并跳转登录页
				authStore.logout();
				return;
			}
		}

		if (dynamicRoutes.value.length === 0) {
			//已登录，并且路由数组为空，则调用获取用户信息接口
			//调用获取用户、角色、菜单、路由、按钮权限等相关配置
			await authStore.getUserInfo();
			// 执行路由注入
			await addDynamicRoutes("mainLayout", dynamicRoutes.value);
			// 通过请求，不过这里需要注意，因为上方进行了路由动态注入，所以这里需要按照新的路由表进行请求，所以不能直接next()
			// 解析当前的请求路径，重新生成一个包含路由匹配信息的对象
			const retryMatch = router.resolve(to.path);
			// 检查重新解析后的路由对象是否匹配到了有效的路由配置。
			if (retryMatch.matched.length > 0) {
				// 表示匹配到了，重新进行跳转
				next(retryMatch);
				return;
			}
		} else {
			//已登录，并且路由数组不为空，表示已经加载过动态路由，直接通过请求即可
			// 通过请求
			next();
		}
	} catch (error) {
		// 清除无效 token 并跳转登录页
		authStore.logout();
	}
});

// 全局错误处理
router.onError((error) => {
	if (error.message.includes("No match")) {
		router.push("/404");
	}
});

// 动态路由注入方法
const addDynamicRoutes = (parentRouter: string, dynamicRoutes: DynamicRoute[]) => {
	// 预加载所有视图组件(动态路由一定要使用这种方式，不能使用)import直接导入的方式，import方式解析模板变量没办法解析多级目录，会报错
	const modules = import.meta.glob(["/src/views/**/*.vue", "/src/layout/**/*.vue"]);
	// 遍历给mainLayout添加子路由
	dynamicRoutes.forEach((dynamicRoute) => {
		// 添加前先检查是否已存在同名路由
		if (!router.hasRoute(dynamicRoute.name)) {
			//递归添加子级路由
			if (
				dynamicRoute.children != null &&
				dynamicRoute.children != undefined &&
				dynamicRoute.children.length > 0
			) {
				router.addRoute(parentRouter, {
					path: dynamicRoute.path,
					name: dynamicRoute.name,
					// 给父级路由添加一个空组件，用于展示子级路由，不写虽然也不会出错，但是会报警告，不太好看
					// 另外这里虽然是直接导入一个静态路径，但是也需要使用modules的方式，如果直接使用import的方式，会报警告，不太好看
					// 同时modules不可以使用@等相对路径，需要使用绝对路径，否则会找不到组件
					component: modules["/src/layout/EmptyRouterView.vue"],
					meta: {
						requiresAuth: true,
						roles: dynamicRoute.meta?.roles || [],
						menuShow: dynamicRoute.meta?.menuShow,
					},
				});
				addDynamicRoutes(dynamicRoute.name, dynamicRoute.children);
			} else {
				router.addRoute(parentRouter, {
					path: dynamicRoute.path,
					name: dynamicRoute.name,
					component: modules[`/src/views/${dynamicRoute.component}.vue`], // 直接匹配预加载的组件
					meta: {
						requiresAuth: true,
						roles: dynamicRoute.meta?.roles || [],
						menuShow: dynamicRoute.meta?.menuShow,
					},
				});
			}
		}
	});

	// 修改通配符路由添加逻辑：
	router.addRoute({
		path: "/:pathMatch(.*)*",
		name: "NotFoundCatchAll",
		redirect: "/404",
	});
};

export default router;
