<template>
	<div class="demo-container">
		<h1>图片上传与预览演示</h1>

		<div class="tutorial-section">
			<h2>使用指南</h2>
			<p>此页面演示了两个功能组件：</p>
			<ul>
				<li><strong>ImageUpload</strong> - 用于上传本地或云端图片</li>
				<li><strong>ImagePreview</strong> - 用于显示单个图片或图片预览列表</li>
			</ul>
			<div class="usage-card">
				<div>
					<h3>ImageUpload 属性</h3>
					<p><code>v-model</code> - 双向绑定已上传的图片URL</p>
					<p><code>:limit</code> - 限制上传数量（默认5张）</p>
					<p><code>:action</code> - 上传图片的接口地址</p>
					<p><code>:data</code> - 上传图片的额外数据</p>
					<p><code>:is-local</code> - 是否使用本地存储（false使用云存储）</p>
					<p><code>:fileSize</code> - 限制上传文件大小（单位：MB，默认5MB）</p>
					<p><code>:fileType</code> - 接受的文件类型（默认"image/*"）</p>
					<p><code>:isShowTip</code> - 是否显示上传提示（默认true）</p>
					<p><code>:disabled</code> - 是否禁用上传（默认false）</p>
					<p><code>:drag</code> - 是否支持拖拽上传（默认true）</p>
				</div>
				<div>
					<h3>ImagePreview 属性</h3>
					<p><code>:src</code> - 图片URL（多个图片用逗号分隔）</p>
					<p><code>:width</code> - 图片显示宽度</p>
					<p><code>:height</code> - 图片显示高度</p>
				</div>
			</div>
		</div>

		<el-row :gutter="20">
			<el-col :span="12">
				<div class="demo-card">
					<h2>图片上传组件</h2>
					<el-form label-width="120px" :model="form" class="demo-form">
						<!-- 上传到云存储 -->
						<el-form-item label="云存储上传">
							<ImageUpload v-model="form.cloudUpload" :is-local="false" :limit="3" :data="{belong: 'article'}"/>
						</el-form-item>

						<!-- 上传到本地存储 -->
						<el-form-item label="本地存储上传">
							<ImageUpload v-model="form.localUpload" :is-local="true" :limit="3" :data="{belong: 'article'}"/>
						</el-form-item>

						<!-- 单图上传 -->
						<el-form-item label="单图上传">
							<ImageUpload v-model="form.singleUpload" :is-local="false" :limit="1" :data="{belong: 'article'}"/>
						</el-form-item>
					</el-form>
				</div>
			</el-col>

			<el-col :span="12">
				<div class="demo-card">
					<h2>图片预览组件</h2>

					<div class="preview-section">
						<h3>单图预览</h3>
						<ImagePreview
							:src="currentImage"
							width="300"
							height="200"
							:is-local="false"
						/>
					</div>

					<div class="preview-section">
						<h3>多图预览（点击图片放大查看）</h3>
						<ImagePreview
							:src="imageList.join(',')"
							width="200"
							height="150"
							:is-local="false"
						/>
					</div>

					<div class="preview-section">
						<h3>图片组合效果</h3>
						<el-row :gutter="10">
							<el-col :span="8">
								<ImagePreview
									:src="imageList[0] || placeholder"
									width="100%"
									height="150"
									:is-local="false"
								/>
							</el-col>
							<el-col :span="8">
								<ImagePreview
									:src="imageList[1] || placeholder"
									width="100%"
									height="150"
									:is-local="false"
								/>
							</el-col>
							<el-col :span="8">
								<ImagePreview
									:src="imageList[2] || placeholder"
									width="100%"
									height="150"
									:is-local="false"
								/>
							</el-col>
						</el-row>
					</div>

					<div class="preview-section">
						<h3>错误状态演示</h3>
						<ImagePreview
							src="non-existent-image.jpg"
							width="200"
							height="150"
							:is-local="false"
						/>
						<p class="error-tip">当图片无法加载时显示错误占位符</p>
					</div>
				</div>
			</el-col>
		</el-row>
	</div>
</template>

<script>
import { defineComponent, reactive, ref } from "vue";

export default defineComponent({
	name: "ImageDemo",
	setup() {
		// 表单数据
		const form = reactive({
			cloudUpload: "",
			localUpload: "",
			singleUpload: "",
		});

		// 演示图片列表（使用阿里云OSS图片）
		const imageList = ref([
			"https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/93abdb7abc9744f6a3b61896abfc185f.jpeg",
			"https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/8d3dbe1ac24d40b9ada3c7e9e67d5f32.jpg",
			"https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/bb5d2f7f78e6432eae19f2a0d35d8e5e.jpg",
		]);

		// 当前显示的图片
		const currentImage = ref(
			"https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/bb5d2f7f78e6432eae19f2a0d35d8e5e.jpg"
		);

		// 占位图片
		const placeholder =
			"https://xueyaxuetang.oss-cn-hangzhou.aliyuncs.com/xyxt_img/article/placeholder.jpg";

		return {
			form,
			imageList,
			currentImage,
			placeholder,
		};
	},
});
</script>

<style scoped>
.demo-container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

h1 {
	text-align: center;
	color: #333;
	margin-bottom: 30px;
	padding-bottom: 15px;
	border-bottom: 1px solid #eee;
}

.tutorial-section {
	background-color: #f8f9fa;
	border-radius: 8px;
	padding: 20px;
	margin-bottom: 30px;
	border: 1px solid #eaeaea;
}

.usage-card {
	display: flex;
	gap: 20px;
	margin-top: 15px;
}

.usage-card > div {
	flex: 1;
	background: white;
	padding: 15px;
	border-radius: 6px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

h2,
h3 {
	color: #2c3e50;
}

.demo-card {
	background: white;
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
	margin-bottom: 20px;
	height: 100%;
}

.demo-form {
	margin-top: 20px;
}

.value-display {
	margin-top: 20px;
	padding: 15px;
	background: #f5f7fa;
	border-radius: 6px;
	max-height: 200px;
	overflow: auto;
	border: 1px solid #ebeef5;
}

pre {
	white-space: pre-wrap;
	word-break: break-all;
}

.preview-section {
	margin-top: 25px;
	padding-bottom: 15px;
	border-bottom: 1px dashed #eaeaea;
}

.preview-section h3 {
	margin-bottom: 10px;
	color: #409eff;
}

.error-tip {
	margin-top: 10px;
	color: #f56c6c;
	font-size: 14px;
}

.el-row {
	margin-bottom: 20px;
}

.el-col {
	display: flex;
	flex-direction: column;
}
</style>
