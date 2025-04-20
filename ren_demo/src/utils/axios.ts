import axios, {
  type AxiosInstance,
  type InternalAxiosRequestConfig,
  type AxiosResponse,
  type AxiosError
} from 'axios';
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/authStore'; // 导入认证 store

declare module 'axios' {
  interface AxiosRequestConfig {
    _retry?: boolean;
  }
}
  
// 创建唯一实例（无拦截器）
export const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 5000
});

// 导出拦截器配置函数（需在应用启动后调用）
export const setupInterceptors = () => {
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

      //请求头添加token进行请求
      
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
  service.interceptors.response.use(
    //请求成功
    async (response: AxiosResponse) => {
      // 1. 获取响应数据中的业务状态码
      const businessCode = response.data.code; 

      // 2. 如果是401业务状态码（需刷新Token）
      if (businessCode === 401) {
        const originalRequest = response.config; // 获取原始请求配置
        // 3. 排除无需认证的路径 & 避免重试循环
        if (
          originalRequest?.url &&
          !UN_AUTH_PATHS.some(path => originalRequest.url?.includes(path)) &&
          !originalRequest._retry
        ) {
          originalRequest._retry = true; // 标记已重试
          try {
            await authStore.refreshToken(); // 刷新Token
            return service(originalRequest); // 重试请求
          } catch (refreshError) {
            authStore.clearAuth(); // 刷新失败则清空登录态
            return Promise.reject(refreshError);
          }
        }
      }
      return response;
    },

    //请求错误
    async (error: AxiosError) => {
      const originalRequest = error.config;
      if (
        error.response?.status === 401 &&                   // 1. 响应状态码为 401（未授权）
        originalRequest?.url &&                             // 2. 原始请求存在 URL
        !UN_AUTH_PATHS.some(path => originalRequest.url?.includes(path)) && // 3. 请求不在排除列表中
        !originalRequest._retry                             // 4. 未重试过
      ) {
        originalRequest._retry = true;//标记请求已重试
        try {
          await authStore.refreshToken();// 调用刷新 Token 的异步方法
          return service(originalRequest);// 重新发送原始请求
        } catch (refreshError) {
          authStore.clearAuth();// 清除认证信息（如跳转登录页）
          return Promise.reject(refreshError);
        }
      }
      return Promise.reject(error);
    }
  );
};