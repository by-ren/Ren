<template>
  <!-- 弹出层组件，点击触发，宽度360，自定义popper类 -->
  <el-popover
    trigger="click"
    :width="360"
    popper-class="icon-picker-popper"
  >
    <!-- 触发元素插槽 -->
    <template #reference>
      <div class="trigger-box">
        <!-- 显示已选图标 -->
        <el-icon v-if="selectedIcon" :size="20" style="margin-right: 10px;">
            <!-- 动态组件显示图标 -->
            <component v-if="selectedIcon && selectedIcon !== '#'" :is="selectedIcon"/>
        </el-icon>
        <!-- 未选择时的提示文字 -->
        <span class="trigger-text">{{ selectedIcon || '请选择图标' }}</span>
      </div>
    </template>

    <!-- 弹出层内容 -->
    <div class="icon-picker-container">
      <!-- 搜索输入框 -->
      <el-input
        v-model="searchText"
        placeholder="搜索图标..."
        clearable
        class="search-input"
      >
        <!-- 输入框前缀图标 -->
        <template #prefix>
          <el-icon><i-ep-search /></el-icon>
        </template>
      </el-input>

      <!-- 图标网格布局 -->
      <div class="icon-grid">
        <!-- 遍历过滤后的图标列表 -->
        <div
          v-for="registeredName in filteredIcons"
          :key="registeredName"
          class="icon-item"
          :class="{ 'active': selectedIcon === registeredName }"
          @click="handleSelect(registeredName)"
        >
          <!-- 鼠标悬停显示图标名称提示 -->
          <el-tooltip :content="registeredName.split('-').slice(2).join('-')" placement="top">
            <el-icon :size="24">
              <!-- 动态显示图标组件 -->
              <component v-if="selectedIcon && selectedIcon !== '#'" :is="registeredName"/>
            </el-icon>
          </el-tooltip>
        </div>
      </div>
    </div>
  </el-popover>
</template>

<script setup lang="ts" name="IconPicker">
// 获取图标原始名称集合
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 组件props定义（接收v-model值）
const props = defineProps<{
  modelValue: string
}>()

// 定义事件发射器
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const searchText = ref('')  // 搜索关键词
// 生成带前缀的图标名称列表
const iconList = ref(
  Object.keys(ElementPlusIconsVue).map(key =>
    `i-ep-${key.toLowerCase()}`
  )
)

// 计算属性：过滤后的图标列表（根据搜索关键词）(监控iconList.value的修改)
const filteredIcons = computed(() => {
  return iconList.value.filter(registeredName =>
    registeredName.toLowerCase().includes(searchText.value.toLowerCase())
  )
})

// 计算属性：双向绑定的选中图标
const selectedIcon = computed({
  get: () => props.modelValue,       // 从props获取值
  set: (val) => emit('update:modelValue', val) // 更新时触发事件
})

// 图标选择处理函数
const handleSelect = (icon: string) => {
  selectedIcon.value = icon  // 更新选中图标
  searchText.value = ''      // 清空搜索条件
}
</script>

<style lang="scss" scoped>
/* 触发区域样式 */
.trigger-box {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 8px;                         /* 元素间距 */
  padding: 8px 12px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.1s;             /* 平滑过渡效果 */

  /* 悬停效果 */
  &:hover {
    border-color: var(--el-color-primary);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  }

  /* 提示文字样式 */
  .trigger-text {
    color: var(--el-text-color-regular);
    font-size: 14px;
  }
}

/* 弹出层内容容器 */
.icon-picker-container {
  padding: 12px;

  /* 搜索框样式 */
  .search-input {
    margin-bottom: 12px;
    /* 深度选择器修改子组件样式 */
    :deep(.el-input__inner) {
      border-radius: 20px;          /* 圆形边框 */
    }
  }

  /* 图标网格布局 */
  .icon-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);  /* 6列网格 */
    gap: 12px;                               /* 网格间距 */
    max-height: 300px;                       /* 最大高度 */
    overflow-y: auto;                        /* 垂直滚动 */

    /* 单个图标项样式 */
    .icon-item {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 12px;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.2s;          /* 过渡动画 */
      background: var(--el-fill-color-light); /* 默认背景 */

      /* 悬停效果 */
      &:hover {
        background: var(--el-color-primary-light-9);
        transform: scale(1.1);       /* 放大效果 */
      }

      /* 选中状态 */
      &.active {
        background: var(--el-color-primary-light-8);
        box-shadow: 0 2px 8px var(--el-color-primary-light-3);
      }
    }
  }
}

/* 全局弹出层样式（非scoped） */
:global(.icon-picker-popper) {
  box-shadow: 0 6px 16px -8px rgba(0, 0, 0, 0.08),
              0 9px 28px 0 rgba(0, 0, 0, 0.05),
              0 12px 48px 16px rgba(0, 0, 0, 0.03) !important;
}
</style>
