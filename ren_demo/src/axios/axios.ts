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
  timeout: 100000
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
        'X-Access-Token',
        accessToken ? `Bearer ${accessToken.value}` : "false accessToken",
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
      let businessCode = response.data.code;

      //浏览器自动把响应头给转成小写了
      const token = response.headers['x-access-token'];
      if(token != undefined && token != null && token != ""){
        // 判断是否存在 "Bearer " 前缀（严格匹配大小写和空格）
        if (token.startsWith("Bearer ")) {
          const pureToken = token.split(" ")[1]; // 提取纯 Token
          //console.log("有效 Token:", pureToken);
          // 将token存储到Pinia
          authStore.accessToken = pureToken;
        } else {
          //console.error("Token 格式错误：缺少 Bearer 前缀");
          // accessToken获取失败，直接使用refreshToken刷新
          businessCode = 401;
        }
      }

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
            return Promise.reject(refreshError);//将异常返回到上层处理
          }
        }
      }
      return response;
    }
  );
};