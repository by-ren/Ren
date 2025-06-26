<template>
	<div class="not-found-container">
		<div class="content-wrapper">
			<!-- 浮动效果插画 -->
			<div class="illustration" :style="floatStyle">
				<img src="https://placehold.co/600x400/EEE/6c5ce7?text=404%20Page&font=roboto" />
			</div>

			<!-- 文字内容区域 -->
			<div class="text-content">
				<h1 class="title">哎呀！页面迷路了</h1>
				<p class="description">您访问的页面可能已被移动、删除或暂时不可用</p>

				<!-- 操作按钮 -->
				<div class="action-buttons">
					<el-button type="primary" class="home-button" @click="goHome">
						<el-icon><i-ep-house /></el-icon>
						返回首页
					</el-button>
					<el-button class="back-button" @click="goBack">
						<el-icon><i-ep-back /></el-icon>
						返回上一页
					</el-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const floatY = ref(0);
let animationFrame: number;

// 浮动动画效果
const floatStyle = computed(() => ({
	transform: `translateY(${floatY.value}px)`,
}));

const animate = () => {
	floatY.value = Math.sin(Date.now() / 800) * 15;
	animationFrame = requestAnimationFrame(animate);
};

onMounted(() => {
	animate();
});

onUnmounted(() => {
	cancelAnimationFrame(animationFrame);
});

// 导航方法
const goHome = () => {
	router.push("/");
};

const goBack = () => {
	router.go(-1);
};
</script>

<style lang="scss" scoped>
@use "sass:color"; // 必须导入

$primary-color: #6c5ce7;
$secondary-color: #a8a5e6;
$background: #f8f9fe;

.not-found-container {
	height: 100vh;
	background: $background;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 2rem;

	.content-wrapper {
		max-width: 1200px;
		display: flex;
		align-items: center;
		gap: 4rem;
		flex-wrap: wrap;
		justify-content: center;

		.illustration {
			flex: 1;
			min-width: 300px;
			max-width: 600px;
			transition: transform 0.3s ease-out;

			img {
				width: 100%;
				height: auto;
			}
		}

		.text-content {
			flex: 1;
			min-width: 300px;

			.title {
				font-size: 2.5rem;
				color: color.adjust($primary-color, $lightness: -15%);
				margin-bottom: 1.5rem;
				font-weight: 700;
			}

			.description {
				font-size: 1.1rem;
				color: color.adjust(#2d3436, $lightness: 20%);
				line-height: 1.6;
				margin-bottom: 2.5rem;
			}
		}

		.action-buttons {
			display: flex;
			gap: 1rem;
			flex-wrap: wrap;

			:deep(.el-button) {
				padding: 12px 24px;
				border-radius: 8px;
				font-weight: 500;
				transition: all 0.3s ease;

				i {
					margin-right: 8px;
				}
			}

			.home-button {
				background: linear-gradient(135deg, $primary-color, $secondary-color);
				border: none;
				color: white;

				&:hover {
					margin-top: -2px; // ✅ 替代方案
					box-shadow: 0 5px 15px rgba($primary-color, 0.3);
				}
			}

			.back-button {
				border-color: $primary-color;
				color: $primary-color;

				&:hover {
					background: rgba($primary-color, 0.05);
				}
			}
		}
	}
}

@media (max-width: 768px) {
	.content-wrapper {
		flex-direction: column;
		text-align: center;

		.action-buttons {
			justify-content: center;
		}
	}
}
</style>
