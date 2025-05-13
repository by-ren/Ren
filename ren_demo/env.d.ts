/// <reference types="vite/client" />

declare module 'element-plus/es/locale/lang/zh-cn';
declare module 'element-plus/dist/locale/zh-cn.mjs';

interface ImportMetaEnv {
    VITE_API_BASE_URL: string;  // 你的环境变量
    VITE_MODE: 'development' | 'production';
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}

declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
}