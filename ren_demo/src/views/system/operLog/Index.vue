<template>
	<el-form
		ref="searchFormRef"
		:rules="rules"
		:inline="true"
		:model="tableParams"
		class="demo-form-inline"
	>
		<el-form-item label="查询" prop="searchLike">
			<el-input
				v-model="tableParams.searchLike"
				placeholder="模块标题/业务类型"
				autocomplete="off"
			></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="search">搜索</el-button>
			<el-button @click="resetForm(searchFormRef)">重置</el-button>
		</el-form-item>
	</el-form>
	<el-table :data="tableData" stripe @sort-change="tableSortHandle">
		<el-table-column prop="title" label="模块标题" show-overflow-tooltip></el-table-column>
		<el-table-column prop="businessType" label="业务类型" width="140" sortable>
			<template #default="item">
				<el-tag v-if="item.row.businessType == 0">其它</el-tag>
				<el-tag v-if="item.row.businessType == 1">新增</el-tag>
				<el-tag v-if="item.row.businessType == 2">修改</el-tag>
				<el-tag v-if="item.row.businessType == 3">删除</el-tag>
			</template>
		</el-table-column>
		<el-table-column
			prop="method"
			label="方法名称"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="requestMethod"
			label="请求方式"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column prop="operatorType" label="操作类别" width="140" sortable>
			<template #default="item">
				<el-tag v-if="item.row.operatorType == 0">其它</el-tag>
				<el-tag v-if="item.row.operatorType == 1">后台用户</el-tag>
				<el-tag v-if="item.row.operatorType == 2">手机端用户</el-tag>
			</template>
		</el-table-column>
		<el-table-column
			prop="operName"
			label="操作人员"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="deptName"
			label="部门名称"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="operUrl"
			label="请求URL"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="operIp"
			label="主机地址"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="operLocation"
			label="操作地点"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="operParam"
			label="请求参数"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="jsonResult"
			label="返回参数"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column prop="isNormal" label="是否正常" width="140" sortable>
			<template #default="item">
				<el-tag v-if="item.row.isNormal == 0">否</el-tag>
				<el-tag v-if="item.row.isNormal == 1">是</el-tag>
			</template>
		</el-table-column>
		<el-table-column
			prop="errorMsg"
			label="错误消息"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="operTimeStr"
			label="操作时间"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column fixed="right" label="操作" min-width="120">
			<template #default="scope">
				<el-button
					link
					type="primary"
					size="small"
					@click="handleDeleteOperLog(scope.$index, scope.row)"
					>删除</el-button
				>
			</template>
		</el-table-column>
	</el-table>
	<Pagination
		:total="total"
		v-model:page="tableParams.pageNum"
		v-model:limit="tableParams.pageSize"
		@pagination="getList"
	/>
</template>

<script setup lang="ts" name="operLog">
import { ref, onMounted } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage, ElTree } from "element-plus";
import { getOperLogList, deleteOperLog } from "@/api/system/operLog/index";
/*============================通用操作日志开始============================*/
//表格数据
let tableData = ref([]);
interface TableParams {
	searchLike: string;
	pageNum: number;
	pageSize: number;
	orderByColumn: string;
	orderByWay: string;
}
//查询操作日志
let tableParams = ref<TableParams>({
	//查询操作日志
	searchLike: "",
	//当前页
	pageNum: 1,
	//每页显示多少条
	pageSize: 10,
	//排序字段
	orderByColumn: "",
	//排序方式
	orderByWay: "",
});
//查询表单
const searchFormRef = ref<FormInstance>();
//表单验证规则（即使用不到，为了重置方法，也需要写）
const rules = reactive<FormRules<TableParams>>({});
//添加或修改(1添加，2修改)
let addOrModifyTag = ref(1);
//总数据数量
const total = ref(0);
//总页数
const totalPage = ref(1);
/*============================通用操作日志结束============================*/

/*============================页面方法开始============================*/
//获取列表
const getList = async () => {
	let result = await getOperLogList(tableParams.value);
	if (result.code == 200) {
		tableData.value = result.rows;
		tableParams.value.pageNum = result.pageNum;
		tableParams.value.pageSize = result.pageSize;
		total.value = result.total;
		totalPage.value = result.totalPage;
	} else {
		ElMessage.error(result.msg);
	}
};
//页面搜索方法
const search = async () => {
	tableParams.value.pageNum = 1;
	getList();
};
//重置
const resetForm = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.resetFields();
};
/*********删除操作日志*********/
const handleDeleteOperLog = async (index: number, row: any) => {
	//调用删除方法
	try {
		let result = await deleteOperLog(row.operId);
		if (result.code == 200) {
			ElMessage({
				message: "删除成功",
				type: "success",
			});
			//重新加载表单
			search();
		} else {
			ElMessage.error(result.msg);
		}
	} catch (error) {
		ElMessage.error("删除失败");
	}
};
/*********排序相关*********/
// 定义前端字段名到数据库字段名的映射
// 注意，这里只需要定义前端页面与数据库字段名不相同的场景，如数据库名为login_date,而前端页面字段名为loginDateStr
// 但是，如果仅仅是驼峰与下划线命名不同，可以不定义，如数据库为login_date，而前端页面字段名为loginDate
// 如果不同且未定义，可能会导致查询失败
const sortFieldMap = {};
const tableSortHandle = (params: {
	column: any;
	prop: keyof typeof sortFieldMap; // 告诉ts，从prop中取出来的值，一定是sortFieldMap的键
	order: string;
}) => {
	// 操作日志说明：
	// - column: 当前列的配置对象
	// - prop: 排序的字段名（对应列的 prop）
	// - order: 排序方式（'ascending' 升序 / 'descending' 降序 / null 默认）

	let orderByColumn = sortFieldMap[params.prop] || params.prop;
	let orderByWay = params.order == null ? "" : params.order == "ascending" ? "asc" : "desc";

	if (orderByColumn != undefined && orderByWay != "") {
		tableParams.value.orderByColumn = orderByColumn;
		tableParams.value.orderByWay = orderByWay;
	} else {
		tableParams.value.orderByColumn = "";
		tableParams.value.orderByWay = "";
	}
	search();
};
/*============================页面方法结束============================*/

/*============================生命周期钩子开始============================*/
// 组件加载完成后执行
// 初始化表格数据
onMounted(async () => {
	search();
});
/*============================生命周期钩子结束============================*/
</script>

<style scoped>
.demo-form-inline .el-input {
	--el-input-width: 220px;
}

.demo-form-inline .el-select {
	--el-select-width: 220px;
}

.btns {
	margin-bottom: 10px;
}
</style>
