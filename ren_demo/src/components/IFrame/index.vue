<template>
  <div v-loading="loading" :style="'height:' + height">
    <iframe
      :src="src"
      frameborder="no"
      style="width: 100%; height: 100%"
      scrolling="auto"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  src: {
    type: String,
    required: true
  }
})

const height = ref(`${document.documentElement.clientHeight - 94.5}px`)
const loading = ref(true)

// 计算高度函数
const calcHeight = () => {
  height.value = `${document.documentElement.clientHeight - 94.5}px`
}

// 处理窗口大小变化
const handleResize = () => {
  calcHeight()
}

onMounted(() => {
  setTimeout(() => {
    loading.value = false
  }, 300)

  calcHeight()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>
