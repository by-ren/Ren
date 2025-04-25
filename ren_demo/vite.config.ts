import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import VueSetupExtend from 'vite-plugin-vue-setup-extend'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import path from 'path';

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    // 配置组件自动导入
    Components({
      // 自动生成 components.d.ts 类型声明文件
      dts: true, 
      // 按需加载 UI 库（例如 Element Plus）
      resolvers: [
        ElementPlusResolver(),
      ],
      // 自动导入自定义组件（src/components 下的组件）
      dirs: ['src/components']
    }),
    VueSetupExtend(),
    // SvgIcon配置插件，指定 SVG 图标目录和 Symbol ID 格式
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
      symbolId: 'icon-[name]', // 符号ID格式，如#icon-user
      svgoOptions: {
        plugins: [{ name: 'removeAttrs', params: { attrs: ['fill'] } }] // 移除默认颜色
      }
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
