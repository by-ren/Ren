/// <reference types="vite/client" />

interface ImportMetaEnv {
    VITE_API_BASE_URL: string;  // 你的环境变量
    VITE_MODE: 'development' | 'production';
    // 其他变量...
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}

declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
  }