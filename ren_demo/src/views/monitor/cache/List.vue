<template>
	<div class="cache-management-container">
		<div class="cache-content">
			<!-- 左侧：缓存列表 -->
			<el-card class="panel-container cache-list">
				<div class="panel-header">
					<h3
						class="panel-title"
						style="display: flex; justify-content: flex-start; align-items: center"
					>
						<el-icon :size="20" style="cursor: pointer; margin-right: 10px"
							><i-ep-collection
						/></el-icon>
						缓存列表
					</h3>
					<div class="panel-actions">
						<div style="display: flex; justify-content: start; align-items: center">
							<el-tooltip content="刷新缓存列表" placement="top">
								<el-icon
									:size="20"
									style="cursor: pointer; margin-right: 10px"
									@click="refreshCacheList"
									><i-ep-refresh
								/></el-icon>
							</el-tooltip>
							<el-tooltip content="清空缓存列表" placement="top">
								<el-icon
									:size="20"
									style="cursor: pointer"
									@click="cleanCacheListHandler"
									><i-ep-close
								/></el-icon>
							</el-tooltip>
						</div>
					</div>
				</div>
				<el-table
					:data="cacheList"
					height="calc(100vh - 260px)"
					highlight-current-row
					@row-click="handleCacheClick"
					:row-style="{ cursor: 'pointer' }"
					style="width: 100%"
				>
					<el-table-column prop="name" label="缓存名称" min-width="140" />
					<el-table-column prop="remark" label="备注" min-width="150" />
					<el-table-column label="操作" width="100">
						<template #default="{ row }">
							<div style="display: flex; justify-content: start; align-items: center">
								<el-tooltip content="删除" placement="top">
									<el-icon
										:size="20"
										style="cursor: pointer"
										@click.stop="deleteCacheHandler(row)"
										><i-ep-delete
									/></el-icon>
								</el-tooltip>
							</div>
						</template>
					</el-table-column>
				</el-table>
			</el-card>

			<!-- 中间：键名列表 -->
			<el-card class="panel-container key-list">
				<div class="panel-header">
					<h3
						class="panel-title"
						style="display: flex; justify-content: flex-start; align-items: center"
					>
						<el-icon :size="20" style="cursor: pointer; margin-right: 10px"
							><i-ep-key
						/></el-icon>
						键名列表
					</h3>
					<div class="panel-actions">
						<div style="display: flex; justify-content: start; align-items: center">
							<el-tooltip content="刷新键值列表" placement="top">
								<el-icon :size="20" style="cursor: pointer" @click="refreshKeys"
									><i-ep-refresh
								/></el-icon>
							</el-tooltip>
						</div>
					</div>
				</div>
				<el-table
					:data="paginatedData"
					height="calc(100vh - 260px)"
					highlight-current-row
					@row-click="handleKeyClick"
					:row-style="{ cursor: 'pointer' }"
					style="width: 100%"
				>
					<el-table-column prop="key" label="键名" min-width="250" />
					<el-table-column label="操作" width="80">
						<template #default="{ row }">
							<div style="display: flex; justify-content: start; align-items: center">
								<el-tooltip content="删除" placement="top">
									<el-icon
										:size="20"
										style="cursor: pointer"
										@click.stop="deleteKeyHandler(row)"
										><i-ep-delete
									/></el-icon>
								</el-tooltip>
							</div>
						</template>
					</el-table-column>
				</el-table>
				<Pagination
					:total="keyList.length"
					v-model:page="pageNum"
					v-model:limit="pageSize"
				/>
			</el-card>

			<!-- 右侧：缓存详情 -->
			<el-card class="panel-container cache-detail">
				<div class="panel-header">
					<h3
						class="panel-title"
						style="display: flex; justify-content: flex-start; align-items: center"
					>
						<el-icon :size="20" style="cursor: pointer; margin-right: 10px"
							><i-ep-document
						/></el-icon>
						缓存详情
					</h3>
					<div class="panel-actions">
						<div style="display: flex; justify-content: start; align-items: center">
							<el-tooltip content="复制" placement="top">
								<el-icon
									:size="20"
									style="cursor: pointer"
									@click="copyCacheContent"
									><i-ep-copydocument
								/></el-icon>
							</el-tooltip>
						</div>
					</div>
				</div>
				<div class="detail-info" v-if="selectedCache && selectedKey && selectedKey.key">
					<div class="info-item">
						<span class="info-label">缓存名称:</span>
						<span class="info-value">{{ selectedCache.name }}</span>
					</div>
					<div class="info-item">
						<span class="info-label">键名:</span>
						<span class="info-value">{{ selectedKey.key }}</span>
					</div>
					<div class="info-item">
						<span class="info-label">过期时间:</span>
						<span class="info-value">2023-12-31 23:59:59</span>
					</div>

					<div class="content-box">
						<h4 class="content-title">缓存内容:</h4>
						<pre class="content-code">{{ selectedKey.value }}</pre>
					</div>
				</div>
				<div v-else class="empty-state">
					<el-icon><i-ep-document /></el-icon>
					<p>请从左侧选择缓存项和键名</p>
				</div>
			</el-card>
		</div>
	</div>
</template>

<script lang="ts" name="cacheList" setup>
import { ref } from "vue";
import {
	listCache,
	listKey,
	deleteCache,
	deleteKey,
	cleanCacheList,
} from "@/api/monitor/cache/list";
import type { Cache, CacheKey } from "@/types/Cache";

/*============================通用参数开始============================*/
// 缓存列表数据
const cacheList = ref<Cache[]>([]);

// 键名列表数据
const keyList = ref<CacheKey[]>([]);

// 当前选中的缓存和键名
const initialSelectedCache = {
	name: "",
	remark: "",
};
const selectedCache = ref<Cache>({ ...initialSelectedCache });
const initialSelectedKey = {
	key: "",
	value: "",
};
const selectedKey = ref<CacheKey>({ ...initialSelectedKey });

//当前页
const pageNum = ref(1);
//每页显示多少条
const pageSize = ref(10);
/*============================通用参数结束============================*/

/*============================页面方法开始============================*/
//获取缓存列表
const listCacheoHandler = async () => {
	selectedCache.value = initialSelectedCache;
	selectedKey.value = initialSelectedKey;
	keyList.value = [];

	const result = await listCache();
	if (result.code == 200) {
		cacheList.value = result.cacheList;
	} else {
		ElMessage.error(result.msg);
	}
};

// 处理缓存点击
const handleCacheClick = async (cache: Cache) => {
	selectedCache.value = cache;
	selectedKey.value = initialSelectedKey;

	const result = await listKey(selectedCache.value.name);
	if (result.code == 200) {
		keyList.value = result.keyList;
	} else {
		ElMessage.error(result.msg);
	}
};

// 处理键名点击
const handleKeyClick = (key: CacheKey) => {
	selectedKey.value = key;
};

// 刷新缓存列表
const refreshCacheList = () => {
	listCacheoHandler();
};

// 刷新键名列表
const refreshKeys = () => {
	if (selectedCache.value != null) {
		handleCacheClick(selectedCache.value);
	} else {
		ElMessage.error("请先选择缓存");
	}
};

// 删除缓存
const deleteCacheHandler = async (cache: Cache) => {
	//调用删除方法
	try {
		const result = await deleteCache(cache.name);
		if (result.code == 200) {
			ElMessage({
				message: "删除成功",
				type: "success",
			});
			refreshCacheList();
		} else {
			ElMessage.error(result.msg);
		}
	} catch (error) {
		ElMessage.error("删除失败");
	}
};

// 删除键名
const deleteKeyHandler = async (key: CacheKey) => {
	//调用删除方法
	try {
		const result = await deleteKey(selectedCache.value.name, key.key);
		if (result.code == 200) {
			ElMessage({
				message: "删除成功",
				type: "success",
			});
			refreshKeys();
		} else {
			ElMessage.error(result.msg);
		}
	} catch (error) {
		ElMessage.error("删除失败");
	}
};

// 复制缓存内容
const copyCacheContent = () => {
	navigator.clipboard
		.writeText(selectedKey.value.value)
		.then(() => {
			ElMessage.success("已复制到剪贴板");
		})
		.catch((err) => {
			console.error("复制失败:", err);
			ElMessage.error("复制失败，请手动选择复制");
		});
};

// 清空缓存列表
const cleanCacheListHandler = async () => {
	//调用删除方法
	try {
		const result = await cleanCacheList();
		if (result.code == 200) {
			ElMessage({
				message: "删除成功",
				type: "success",
			});
			refreshKeys();
		} else {
			ElMessage.error(result.msg);
		}
	} catch (error) {
		ElMessage.error("清空失败");
	}
};
/*============================页面方法结束============================*/

/*============================计算属性开始============================*/
// 核心计算属性：计算当前页数据
const paginatedData = computed(() => {
	const start = (pageNum.value - 1) * pageSize.value;
	const end = start + pageSize.value;
	return keyList.value.slice(start, end);
});
/*============================计算属性结束============================*/

/*============================生命周期钩子开始============================*/
// 组件加载完成后执行
onMounted(async () => {
	listCacheoHandler();
});
/*============================生命周期钩子结束============================*/
</script>

<style scoped>
.cache-management-container {
	padding: 20px;
	height: 100%;
	background-color: #f5f7fa;
}

.page-header {
	margin-bottom: 20px;
	background: linear-gradient(to right, #f0f7ff, #ffffff);
	border: none;
}

.page-header h2 {
	margin: 0;
	color: #303133;
	font-size: 22px;
}

.cache-content {
	display: grid;
	grid-template-columns: 1fr 1fr 1.2fr;
	gap: 20px;
	height: calc(100vh - 120px);
}

.panel-container {
	border-radius: 6px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
	height: 100%;
	display: flex;
	flex-direction: column;
}

.panel-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding-bottom: 15px;
	margin-bottom: 10px;
	border-bottom: 1px solid #ebeef5;
}

.panel-title {
	margin: 0;
	font-size: 16px;
	font-weight: 600;
	color: #303133;
}

.detail-info {
	padding: 15px 0;
}

.info-item {
	display: flex;
	margin-bottom: 12px;
	padding-bottom: 12px;
	border-bottom: 1px dashed #ebeef5;
}

.info-label {
	width: 90px;
	font-weight: 500;
	color: #606266;
}

.info-value {
	flex: 1;
	color: #303133;
	word-break: break-all;
}

.content-title {
	margin: 20px 0 10px;
	color: #303133;
	font-size: 15px;
	font-weight: 600;
}

.content-box {
	background-color: #f8f9fa;
	border-radius: 6px;
	padding: 15px;
	border: 1px solid #ebeef5;
}

.content-code {
	margin: 0;
	font-family: "Courier New", monospace;
	font-size: 13px;
	line-height: 1.5;
	color: #333;
	max-height: calc(100vh - 420px);
	overflow: auto;
	white-space: pre-wrap;
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	height: calc(100vh - 280px);
	color: #909399;
}

.empty-state .el-icon {
	font-size: 48px;
	margin-bottom: 15px;
	opacity: 0.6;
}

.empty-state p {
	margin: 0;
	font-size: 14px;
}

.cache-detail {
	overflow-y: scroll;
}
</style>
