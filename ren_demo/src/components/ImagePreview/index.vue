<template>
	<el-image
		:src="realSrc"
		fit="cover"
		:style="`width:${realWidth};height:${realHeight};`"
		:preview-src-list="realSrcList"
	>
		<template #error>
			<div class="image-slot">
				<el-icon style="font-size: 70px">
					<i-ep-picturerounded />
				</el-icon>
			</div>
		</template>
	</el-image>
</template>

<script setup name="ImagePreview">
import { computed } from "vue";
import { isExternal } from "@/utils/validate";

// 定义组件属性
const props = defineProps({
	src: {
		type: String,
		default: "",
	},
	width: {
		type: [Number, String],
		default: "",
	},
	height: {
		type: [Number, String],
		default: "",
	},
	// 是否本地（true：本地，false：云）
	isLocal: {
		type: Boolean,
		default: true,
	},
});

// 计算真实图片源
const realSrc = computed(() => {
	if (!props.src) return "";
	const real_src = props.src.split(",")[0];
	if (isExternal(real_src)) return real_src;

	const baseUrl = props.isLocal
		? import.meta.env.VUE_APP_BASE_API
		: import.meta.env.VUE_APP_CLOUD_PIC_URL + "/";

	return baseUrl + real_src;
});

// 计算预览图片列表
const realSrcList = computed(() => {
	if (!props.src) return [];

	return props.src.split(",").map((item) => {
		if (isExternal(item)) return item;
		const baseUrl = props.isLocal
			? import.meta.env.VUE_APP_BASE_API
			: import.meta.env.VUE_APP_CLOUD_PIC_URL + "/";
		return baseUrl + item;
	});
});

// 计算真实宽度
const realWidth = computed(() =>
	typeof props.width === "string" ? props.width : `${props.width}px`
);

// 计算真实高度
const realHeight = computed(() =>
	typeof props.height === "string" ? props.height : `${props.height}px`
);
</script>

<style scoped>
.el-image {
	border-radius: 5px;
	background-color: #ebeef5;
	box-shadow: 0 0 5px 1px #ccc;
}

:deep(.el-image__inner) {
	transition: all 0.3s;
	cursor: pointer;
}
:deep(.el-image__inner:hover) {
	transform: scale(1.2);
}

.image-slot {
	display: flex;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 100%;
	color: #909399;
	font-size: 30px;
}
</style>
