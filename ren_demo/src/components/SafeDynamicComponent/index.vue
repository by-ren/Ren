<template>
  <!--
    使用动态组件，如果组件存在才渲染
    :is 绑定传入的组件名称
    v-if 根据组件存在性决定是否渲染
  -->
  <component :is="componentName" v-if="componentExists" :style="{width, height}">
  </component>
</template>

<script setup>
import { computed, getCurrentInstance } from 'vue'

// 定义组件的 props
const props = defineProps({
  // 需要动态渲染的组件名称（必传）
  componentName: {
    type: String,    // 类型为字符串
    required: true   // 必须传入该属性
  },
  // 组件宽度
  width: {
    type: String,    // 类型为字符串
    required: false,   // 必须传入该属性
    default: '24px'   // 默认值为 '24px'
  },
  // 组件高度
  height: {
    type: String,    // 类型为字符串
    required: false,   // 必须传入该属性
    default: '24px'   // 默认值为 '24px'
  }
})

// 获取当前组件实例
// Vue 3 的 <script setup> 中不能使用 this，必须通过 getCurrentInstance 获取实例
const instance = getCurrentInstance()

// 计算属性：检查组件是否存在
const componentExists = computed(() => {
  /**
   * 检查逻辑：
   * 1. 通过当前实例获取全局组件注册表
   * 2. 使用 in 操作符检查传入的组件名是否在注册表中
   *
   * 注意：在 Vue 3 中：
   * - 所有注册的组件（全局注册和局部注册）都可通过全局组件注册表访问
   * - instance?.appContext?.components 获取全局注册表对象
   * - ?. 操作符进行可选链访问，避免未定义错误
   * - || {} 提供空对象作为后备值
   */
  const globalComponents = instance?.appContext?.components || {}

  // 返回检查结果：true 表示组件存在，false 表示不存在
  return props.componentName in globalComponents
})
</script>
