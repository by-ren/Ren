import { defineStore } from 'pinia'
import { ref } from 'vue'
import { service } from '@/axios/axios'
import type { User } from '@/types/User'
import type { LoginResponse } from '@/types/LoginResponse'
import type { DynamicRoute } from '@/types/DynamicRoute'
import type { Menu } from '@/types/Menu'
import router from '@/router';


export const useAuthStore = defineStore('auth', () => {
    // 状态声明
    // 登录用户信息
    const user = ref<User | null>(null)     
    // 短期token    
    const accessToken = ref("")
    // 是否登录
    const isLogin = ref(false)
    // 用户角色
    const roles = ref<string[]>([])
    // 按钮权限
    const permissions = ref<string[]>([])
    // 用户角色Id
    const roleIds = ref<number[]>([])
    // 侧边栏菜单
    const menus = ref<Menu[]>([])
    // 路由配置
    const dynamicRoutes = ref<DynamicRoute[]>([])

    //刷新token
    const refreshToken = async (): Promise<boolean> => {
        //从localStorage中获取到refreshToken
        const refreshToken = localStorage.getItem('refreshToken');
        //如果没有获取到，则报错
        if (!refreshToken) {
            clearAuth();
            throw new Error('无有效 RefreshToken')
        };

        try {
            //向refreshToken发送请求，要求返回值是RefreshTokenResponse类型
            const response = await service.post(
                '/auth/refreshToken', 
                {},
                { headers: { 'X-Refresh-Token': refreshToken } }
            );
            //从响应头中获取到新的accessToken
            const newAccessToken = response.data.accessToken;
            //如果不存在，则抛错
            if (!newAccessToken) throw new Error('无效的令牌响应');

            //如果存在，则将新的accessToken存入Pinia
            accessToken.value = newAccessToken
            //如果refreshToken存在，将新的refreshToken存入localStorage
            response.data.refreshToken && localStorage.setItem('refreshToken', response.data.refreshToken);

            return true;
        } catch (err) {
            clearAuth();
            throw new Error('令牌刷新失败');
        }
    }

    // 登录方法
    const login = async (username: string, password: string): Promise<LoginResponse> => {
        //向/login接口发送请求，要求返回体是LoginResponse类型
        const response = await service.post<LoginResponse>('/auth/login', { username, password });
        //如果请求成功
        if (response.data.code === 200) {
            //将accessToken存入Pinia
            accessToken.value = response.data.accessToken
            //如果refreshToken存在，将refreshToken存入localStorage
            response.data.refreshToken && localStorage.setItem('refreshToken', response.data.refreshToken);
            //是否已经登陆设置为是
            isLogin.value = true;
        }
        return response.data;
    }

    // 自动登录方法
    const autoLogin = async () => {
        const response = await service.post('/auth/auto/login');
        //如果请求成功
        if (response.data.code === 200) {
            //是否已经登陆设置为是
            isLogin.value = true;
        }
        return response.data;
    }

    //获取用户信息接口
    const getUserInfo = async (): Promise<void> => {
        try {
            const res = await service.get('/user/info');
            //该接口包含自动登录功能，所以是否已经登陆设置为是
            isLogin.value = true;
            //以下设置用户相关信息
            user.value = res.data.user;
            roles.value = res.data.roles;
            permissions.value = res.data.permissions;
            roleIds.value = res.data.roleIds;
            menus.value = res.data.menus;
            dynamicRoutes.value = res.data.dynamicRoutes;
        } catch (err: unknown) {
            clearAuth();
            throw err; // 抛出错误让路由守卫处理
        }
    }

    //用户登出
    const logout = async (): Promise<void> => {
        await service.post('/auth/logout');
        clearAuth();
    }

    //清除登录信息
    const clearAuth = (): void => {
        user.value = null;
        roles.value = [];
        permissions.value = [];
        roleIds.value = [];
        menus.value = [];
        dynamicRoutes.value = [];
        accessToken.value = "";
        isLogin.value = false;
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('tagArr');
        router.replace('/login');
    }

    return {
        // 状态
        user,
        accessToken,
        isLogin,
        roles,
        permissions,
        roleIds,
        menus,
        dynamicRoutes,

        // 方法
        login,
        refreshToken,
        getUserInfo,
        logout,
        clearAuth,
        autoLogin
    }
})