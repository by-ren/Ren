import { defineStore } from 'pinia'
import { ref } from 'vue'
import { service } from '@/utils/axios'
import type { User } from '@/types/User'
import type { LoginResponse } from '@/types/LoginResponse'
import type { RefreshTokenResponse } from '@/types/RefreshTokenResponse'
import { useRouter } from 'vue-router'


export const useAuthStore = defineStore('auth', () => {
    // 状态声明
    const user = ref<User | null>(null)          // 登录用户信息
    const accessToken = ref("")          // 短期token
    const isAuthenticated = ref(false)
    // 登录方法
    const login = async (username: string, password: string): Promise<LoginResponse> => {
        //向/login接口发送请求，要求返回体是LoginResponse类型
        const response = await service.post<LoginResponse>('/login', { username, password });
        //如果请求成功
        if (response.data.code === 200) {
            //将accessToken存入Pinia
            accessToken.value = response.data.token
            //如果refreshToken存在，将refreshToken存入localStorage
            response.data.refreshToken && localStorage.setItem('refreshToken', response.data.refreshToken);
            //将user存入Pinia
            user.value = response.data.user;
        }
        return response.data;
    }

    //刷新token
    const refreshToken = async (): Promise<boolean> => {
        //从localStorage中获取到refreshToken
        const refreshToken = localStorage.getItem('refreshToken');
        //如果没有获取到，则报错
        if (!refreshToken) throw new Error('无有效 RefreshToken');

        try {
            //向refreshToken发送请求，要求返回值是RefreshTokenResponse类型
            const response = await service.post<RefreshTokenResponse>(
                '/refreshToken', 
                {},
                { headers: { 'X-Refresh-Token': refreshToken } }
            );

            //从响应头中获取到新的accessToken
            const newAccessToken = response.headers.authorization?.split(' ')[1];
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

    //获取用户信息接口
    const getUserInfo = async (): Promise<void> => {
        try {
            const res = await service.get('/user/info');
            user.value = res.data.user;
            isAuthenticated.value = true;
        } catch (err: unknown) {
            clearAuth();
        }

    }

    //用户登出
    const logout = async (): Promise<void> => {
        await service.post('/logout');
        clearAuth();
    }

    //清除登录信息
    const clearAuth = (): void => {
        user.value = null;
        accessToken.value = "";
        isAuthenticated.value = false;
        localStorage.removeItem('refreshToken');
        const router = useRouter()

        window.location.href = '/login';
    }

    return {
        // 状态
        user,
        accessToken,
        isAuthenticated,

        // 方法
        login,
        refreshToken,
        getUserInfo,
        logout,
        clearAuth
    }
})