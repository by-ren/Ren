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
  //配置路径映射，@对应到src目录
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  plugins: [
    vue(),
    //Vue开发工具辅助（注意，一旦开启这个，浏览器插件将失效，因为会打架，但是没关系，这个更好）（同时注意，这个插件只能在Vite+Vue3.3+以上版本才可以用，低版本的还是只能用浏览器自带插件）
    vueDevTools(),
    VueSetupExtend(),

    // 配置API自动导入
    AutoImport({
      // 自动导入 Vue 相关函数，如：ref, reactive, toRef 等
      imports: ['vue'],
      // 按需加载 Element Plus 所需函数
      resolvers: [
        // 自动按需导入 Element Plus 函数，如ElMessageBox
        ElementPlusResolver(),
        // 自动导入图标组件（这里对应的是JS中的导入）
        // 相当于替代了 import IconEpEdit from 'virtual:icons/ep/edit';
        // 可以直接在JS中使用 const edIcon = IconEpEdit（注意应为Icon + 驼峰式图标名）
        IconsResolver({
          prefix: 'Icon', // JS变量名前缀（驼峰式）如（IconEpEdit）
          enabledCollections: ['ep'] // 只导入Element Plus图标集
        }),
      ],
      // 生成 auto-imports.d.ts 类型声明文件
      dts: true, 
    }),

    // 配置组件自动导入
    Components({
      // 按需加载 UI 库（例如 Element Plus）
      resolvers: [
        // 自动按需导入 Element Plus 组件，如<el-menu></el-menu>
        ElementPlusResolver(), 
        // 自动导入图标组件（这里对应的是模板中的导入）
        // 相当于替代了 import { House } from '@element-plus/icons-vue';
        // 可以直接在模板中使用 <el-icon><i-ep-house /></el-icon>（注意应为i- + 连字符图标名）
        IconsResolver({
          prefix: 'i', // 图标组件前缀（对应菜单中的 `i-ep-house`）
          enabledCollections: ['ep'], // 启用 Element Plus 图标集
          // 图标名称转换规则
          alias: {
            'ep': 'element-plus' // 将 'ep' 映射到 @element-plus/icons-vue
          }
        })
      ],

      // 自动导入自定义组件（src/components 下的组件）
      dirs: [
        'src/components',
        'src/components/**' // 递归扫描子目录
      ],
      // 启用深度扫描
      deep: true, 

      // 使用默认配置，自动生成 components.d.ts 类型声明文件
      dts: true,
      // 指定声明文件的生成目录，即生成到 src/auto-imports.d.ts
      //dts: path.resolve(pathSrc, 'auto-imports.d.ts') 
    }),

    // 图标加载器
    Icons({
      compiler: 'vue3',
      autoInstall: true,// 自动安装图标库
      scale: 1, // 确保图标缩放比例正确
      defaultClass: 'ep-icon', // 添加默认类名（可用于加样式）
    }),

    // 配置样式自动导入（各组件不一样需要单独配置）
    ElementPlus({
      useSource: true,
    }),

    // SvgIcon配置插件，指定 SVG 图标目录和 Symbol ID 格式
    // 将自己本地静态的icon图标（svg后缀）生成雪碧图符号
    // 需配合 <use xlink:href="#icon-user"> 使用，依赖 CSS 覆盖原生属性（如 fill）
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')], //指定静态图标目录
      symbolId: 'icon-[name]', // 符号ID格式，如#icon-user
      svgoOptions: {
        plugins: [{ name: 'removeAttrs', params: { attrs: ['fill'] } }] // 移除默认颜色
      }
    }),
  ],
})
