import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import ElementPlus from 'unplugin-element-plus/vite'
import AutoImport from 'unplugin-auto-import/vite'
import VueSetupExtend from 'vite-plugin-vue-setup-extend'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import path from 'path';
import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    VueSetupExtend(),
    // 配置API自动导入
    AutoImport({
      imports: [
        'vue',
        {
          'element-plus': ['ElMessageBox'], // 自动导入 Element Plus 的 API
        },
      ],
      dts: true, // 生成 auto-imports.d.ts 类型声明文件
    }),
    // 配置组件自动导入
    Components({
      // 自动生成 components.d.ts 类型声明文件
      dts: true, 
      // 按需加载 UI 库（例如 Element Plus）
      resolvers: [
        // 自动按需导入 Element Plus 组件
        ElementPlusResolver(), 
        // 图标自动导入
        IconsResolver({
          prefix: 'i', // 图标组件前缀（对应菜单中的 `i-ep-house`）
          enabledCollections: ['ep'] // 启用 Element Plus 图标集
        })
      ],
      // 自动导入自定义组件（src/components 下的组件）
      dirs: ['src/components']
    }),
    //配置图标自动导入
    // 其他插件...
    Icons({
      compiler: 'vue3',
      autoInstall: true,
      scale: 1, // 确保图标缩放比例正确
      defaultClass: 'ep-icon', // 添加默认类名（可选）
    }),
    // 配置样式自动导入（和组件不一样需要单独配置）
    ElementPlus({
      useSource: true,
    }),
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
