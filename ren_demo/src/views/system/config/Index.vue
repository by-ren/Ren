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
				placeholder="参数名称/参数键名"
				autocomplete="off"
			></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="search">搜索</el-button>
			<el-button @click="resetForm(searchFormRef)">重置</el-button>
		</el-form-item>
	</el-form>
	<el-row class="btns">
		<el-col :span="24">
			<el-button type="primary" @click="openAddConfigDialog">添加参数</el-button>
		</el-col>
	</el-row>
	<el-table :data="tableData" stripe @sort-change="tableSortHandle">
		<el-table-column prop="configName" label="参数名称" show-overflow-tooltip></el-table-column>
		<el-table-column
			prop="configKey"
			label="参数键名"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="configValue"
			label="参数键值"
			width="200"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column prop="isSystem" label="是否系统内置" width="140" sortable>
			<template #default="item">
				<el-tag v-if="item.row.isSystem == 0">否</el-tag>
				<el-tag v-if="item.row.isSystem == 1">是</el-tag>
			</template>
		</el-table-column>
		<el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
		<el-table-column fixed="right" label="操作" min-width="120">
			<template #default="scope">
				<el-button
					link
					type="primary"
					size="small"
					@click="openModifyConfigDialog(scope.$index, scope.row)"
					>修改</el-button
				>
				<el-button
					link
					type="primary"
					size="small"
					@click="handleDeleteConfig(scope.$index, scope.row)"
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
	<el-dialog
		:title="addOrModifyTag == 1 ? '添加参数' : '修改参数'"
		v-model="dialogFormAddOrModifyConfig"
		width="500px"
	>
		<el-form
			:model="addOrModifyConfigForm"
			:rules="addOrModifyConfigFormRules"
			ref="addOrModifyConfigFormRef"
		>
			<el-form-item
				label="参数名称"
				:label-width="addOrModifyConfigFormLabelWidth"
				prop="configName"
			>
				<el-input v-model="addOrModifyConfigForm.configName" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item
				label="参数键名"
				:label-width="addOrModifyConfigFormLabelWidth"
				prop="configKey"
			>
				<el-input v-model="addOrModifyConfigForm.configKey" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item
				label="参数键值"
				:label-width="addOrModifyConfigFormLabelWidth"
				prop="configValue"
			>
				<el-input v-model="addOrModifyConfigForm.configValue" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item
				label="是否系统内置"
				:label-width="addOrModifyConfigFormLabelWidth"
				prop="isSystem"
			>
				<el-radio-group v-model="addOrModifyConfigForm.isSystem" size="large" fill="#6cf">
					<el-radio-button label="是" :value="1" />
					<el-radio-button label="否" :value="0" />
				</el-radio-group>
			</el-form-item>
			<el-form-item label="备注" :label-width="addOrModifyConfigFormLabelWidth" prop="remark">
				<el-input v-model="addOrModifyConfigForm.remark" autocomplete="off"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="addOrModifyConfigCancel">取 消</el-button>
				<el-button
					type="primary"
					@click="addOrModifyConfigConfirm(addOrModifyConfigFormRef)"
				>
					确 定
				</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup lang="ts" name="config">
import { ref, onMounted } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage, ElTree } from "element-plus";
import { getConfigList, addConfig, modifyConfig, deleteConfig } from "@/api/system/config/index";
/*============================通用参数开始============================*/
//表格数据
let tableData = ref([]);
interface TableParams {
	searchLike: string;
	pageNum: number;
	pageSize: number;
	orderByColumn: string;
	orderByWay: string;
}
//查询参数
let tableParams = ref<TableParams>({
	//查询参数
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
/*============================通用参数结束============================*/

/*============================页面方法开始============================*/
//获取列表
const getList = async () => {
	let result = await getConfigList(tableParams.value);
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
/*********添加修改参数*********/
//弹出框是否显示
let dialogFormAddOrModifyConfig = ref(false);
//表单元素宽度
let addOrModifyConfigFormLabelWidth = ref("110px");
//添加参数表单初始值
const initialAddOrModifyConfigForm = {
	//参数主键
	configId: 0,
	//参数名称
	configName: "",
	//是否系统内置
	isSystem: 0,
	//参数键名
	configKey: "",
	//参数键值
	configValue: "",
	//备注
	remark: "",
};
//添加参数表单对象
const addOrModifyConfigForm = ref({ ...initialAddOrModifyConfigForm });
//添加参数表单对象
const addOrModifyConfigFormRef = ref<FormInstance>();
//添加参数表单验证规则
const addOrModifyConfigFormRules = reactive<FormRules>({
	configName: [{ required: true, message: "请填写参数名称", trigger: "blur" }],
	configKey: [{ required: true, message: "请填写参数键名", trigger: "blur" }],
	configValue: [{ required: true, message: "请填写参数键值", trigger: "blur" }],
});

//打开添加弹框
const openAddConfigDialog = async () => {
	//弹出框设置为添加弹框
	addOrModifyTag.value = 1;
	//表单值恢复为初始值
	addOrModifyConfigForm.value = { ...initialAddOrModifyConfigForm };
	//清除验证状态
	addOrModifyConfigFormRef.value?.clearValidate();
	//添加表单的主键设置为0
	addOrModifyConfigForm.value.configId = 0;
	//显示弹出框
	dialogFormAddOrModifyConfig.value = true;
};
//打开修改弹框
const openModifyConfigDialog = async (index: number, row: any) => {
	//弹出框设置为修改弹框
	addOrModifyTag.value = 2;
	//表单值恢复为初始值
	addOrModifyConfigForm.value = { ...initialAddOrModifyConfigForm };
	//清除验证状态
	addOrModifyConfigFormRef.value?.clearValidate();
	//显示弹出框
	dialogFormAddOrModifyConfig.value = true;
	//设置弹出框中相关值
	addOrModifyConfigForm.value.configId = row.configId;
	addOrModifyConfigForm.value.configName = row.configName;
	addOrModifyConfigForm.value.isSystem = row.isSystem;
	addOrModifyConfigForm.value.configKey = row.configKey;
	addOrModifyConfigForm.value.configValue = row.configValue;
	addOrModifyConfigForm.value.remark = row.remark;
};
//实现添加或修改
const addOrModifyConfigConfirm = async (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	await formEl.validate(async (valid, fields) => {
		if (valid) {
			//调用添加或修改方法
			if (addOrModifyTag.value == 1) {
				let result = await addConfig(addOrModifyConfigForm.value);
				if (result.code == 200) {
					ElMessage({
						message: "添加成功",
						type: "success",
					});
					//关闭弹框
					dialogFormAddOrModifyConfig.value = false;
					//表单值恢复为初始值
					addOrModifyConfigForm.value = { ...initialAddOrModifyConfigForm };
					//重新加载表单
					search();
				} else {
					ElMessage.error(result.msg);
				}
			} else {
				let result = await modifyConfig(addOrModifyConfigForm.value);
				if (result.code == 200) {
					ElMessage({
						message: "修改成功",
						type: "success",
					});
					//关闭弹框
					dialogFormAddOrModifyConfig.value = false;
					//表单值恢复为初始值
					addOrModifyConfigForm.value = { ...initialAddOrModifyConfigForm };
					//重新加载表单
					search();
				} else {
					ElMessage.error(result.msg);
				}
			}
		}
	});
};
//取消弹框
const addOrModifyConfigCancel = async () => {
	//关闭弹框
	dialogFormAddOrModifyConfig.value = false;
	//表单值恢复为初始值
	addOrModifyConfigForm.value = { ...initialAddOrModifyConfigForm };
};
/*********删除参数*********/
const handleDeleteConfig = async (index: number, row: any) => {
	//调用删除方法
	try {
		let result = await deleteConfig(row.configId);
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
	// 参数说明：
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
