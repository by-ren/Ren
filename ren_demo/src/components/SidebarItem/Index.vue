<template>
	<div>
		<!-- 有子菜单的情况 -->
		<el-sub-menu v-if="item.children" :index="item.index">
			<!-- 渲染分组项 -->
			<template #title>
				<el-icon>
					<!-- 动态绑定目标组件，此种方式依赖于main.ts中的全局注册，所以一定要先注册之后再使用动态绑定 -->
					<SafeDynamicComponent :componentName="item.icon" width="20px" height="20px" />
				</el-icon>
				<span>{{ item.name }}</span>
			</template>
			<!-- 渲染子菜单项 -->
			<sidebar-item
				v-for="child in item.children"
				:key="child.index"
				:item="child"
			></sidebar-item>
		</el-sub-menu>
		<!-- 无子菜单的情况 -->
		<el-menu-item v-else :index="item.index">
			<el-icon>
				<!-- 动态绑定目标组件，此种方式依赖于main.ts中的全局注册，所以一定要先注册之后再使用动态绑定 -->
				<!-- 使用自定义动态绑定组件，可以判断组件是否存在，防止报错 -->
				<SafeDynamicComponent :componentName="item.icon" width="20px" height="20px" />
			</el-icon>
			<span>{{ item.name }}</span>
		</el-menu-item>
	</div>
</template>

<script setup lang="ts" name="SidebarItem">
const props = defineProps({
	item: {
		required: true,
		type: Object,
	},
});
</script>

<style scoped></style>
