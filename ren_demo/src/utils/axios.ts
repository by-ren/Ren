import axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  type InternalAxiosRequestConfig,
  type AxiosResponse,
  type AxiosError
} from 'axios';
import { useAuthStore } from '@/stores/authStore'; // 导入认证 store
import { storeToRefs } from 'pinia'

declare module 'axios' {
  interface AxiosRequestConfig {
    _retry?: boolean;
  }
}

const createService = (): AxiosInstance => {
  const service = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 5000
  });

  //创建拦截器
  const setupInterceptors = () => {

    const UN_AUTH_PATHS: readonly string[] = ['/auth/login', '/auth/refreshToken'];
    const authStore = useAuthStore(); // 初始化 store
  
    // 请求前的拦截器，用于加AccessToken
    service.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        if (!config.url) return config;
    
        // 跳过无需认证的接口
        if (UN_AUTH_PATHS.some(path => config.url?.includes(path))) {
          return config;
        }
    
        const { accessToken } = storeToRefs(authStore)
        // 安全处理 headers（类型兼容的关键）
        config.headers.set(
          'Authorization',
          accessToken ? `Bearer ${accessToken}` : "false accessToken",
          true // 允许覆盖已有值
        );
    
        return config;
      }
    );
  
    //请求后Token，用于处理401错误，从而刷新Token，或者跳转到登录页
    service.interceptors.response.use((response: AxiosResponse) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config;
  
        if (
          error.response?.status === 401 && originalRequest?.url && !UN_AUTH_PATHS.some(path => originalRequest.url?.includes(path)) && !originalRequest._retry
        ) {
          originalRequest._retry = true;
          
          try {
            await authStore.refreshToken();
            return service(originalRequest);
          } catch (refreshError) {
            authStore.clearAuth();
            return Promise.reject(refreshError);
          }
        }
  
        return Promise.reject(error);
      }
    );

  }

  // 在应用初始化完成后调用
  setTimeout(setupInterceptors, 0)

  return service;
};

export const service = createService();