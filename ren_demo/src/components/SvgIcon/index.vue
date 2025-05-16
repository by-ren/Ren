<template>
  <!-- 判断是否为外部链接图标，如果是，直接用DIV+CSS渲染 -->
  <div v-if="isExternal" :style="styleExternalIcon" class="svg-external-icon svg-icon" v-bind="$attrs" />
  <!-- 如果不是，则使用svg渲染器渲染 -->
  <svg v-else :class="svgClass" aria-hidden="true" v-bind="$attrs">
    <use :xlink:href="iconName" />
  </svg>
</template>

<script setup name="SvgIcon">
import { computed } from 'vue';
import { isExternal as checkExternal } from '@/utils/validate';

// iconClass必传，className可空
const props = defineProps({
  iconClass: {
    type: String,
    required: true
  },
  className: {
    type: String,
    default: ''
  }
});

// 判断是否为外部链接图标
const isExternal = computed(() => checkExternal(props.iconClass));
// 拼接iconClass
const iconName = computed(() => `#icon-${props.iconClass}`);
// 拼接className
const svgClass = computed(() => 
  props.className ? 'svg-icon ' + props.className : 'svg-icon'
);

const styleExternalIcon = computed(() => ({
  mask: `url(${props.iconClass}) no-repeat 50% 50%`,
  '-webkit-mask': `url(${props.iconClass}) no-repeat 50% 50%`
}));
</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.svg-external-icon {
  background-color: currentColor;
  mask-size: cover!important;
  display: inline-block;
}
</style>