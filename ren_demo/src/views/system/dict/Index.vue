<template>
	<el-tabs 
	v-model="activeName" 
	class="demo-tabs" 
	@tab-click="handleClick"
	>
		<el-tab-pane label="字典类型" name="dictType" />
		<el-tab-pane label="字典数据" name="dictData" />
	</el-tabs>
	<RouterView />
</template>

<script lang="ts" setup name="dict">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { TabsPaneContext } from 'element-plus'

const activeName = ref('dictType')
const route = useRoute()
const router = useRouter()

// 路由变化同步 Tab 状态
watch(() => route.path, (newPath) => {
  activeName.value = newPath.includes('dictData') ? 'dictData' : 'dictType'
}, { immediate: true })

// Tab 点击切换路由
const handleClick = (tab: TabsPaneContext) => {
  const path = tab.props.name === 'dictType' 
    ? '/system/dict/dictType' 
    : '/system/dict/dictData'
  
  router.replace(path)
}
</script>

<style>
/* 隐藏 el-tabs 的内容区域 */
.demo-tabs .el-tabs__content {
  display: none;
}

/* 可选：调整 Tab 样式 */
.demo-tabs .el-tabs__header {
  margin: 0;
  padding: 0 0 20px 0;
}
</style>