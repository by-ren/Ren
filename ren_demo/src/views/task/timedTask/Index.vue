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
				placeholder="定时任务名称/定时任务编码"
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
			<el-button type="primary" @click="openAddTimedTaskDialog">添加定时任务</el-button>
		</el-col>
	</el-row>
	<el-table :data="tableData" stripe @sort-change="tableSortHandle">
		<el-table-column
			prop="taskName"
			label="任务名称"
			width="140"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="taskGroup"
			label="任务组名"
			width="140"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column
			prop="invokeTarget"
			label="调用目标字符串"
			width="300"
			show-overflow-tooltip
			sortable="custom"
		></el-table-column>
		<el-table-column
			prop="cronExpression"
			label="cron执行表达式"
			width="300"
			show-overflow-tooltip
			sortable="custom"
		></el-table-column>
		<el-table-column prop="misfirePolicy" label="计划执行错误策略" width="200" sortable>
			<template #default="item">
				<el-tag v-if="item.row.misfirePolicy == 0">默认</el-tag>
				<el-tag v-if="item.row.misfirePolicy == 1">立即触发执行</el-tag>
				<el-tag v-if="item.row.misfirePolicy == 2">触发一次执行</el-tag>
				<el-tag v-if="item.row.misfirePolicy == 3">不触发立即执行</el-tag>
			</template>
		</el-table-column>
		<el-table-column prop="concurrent" label="是否并发执行" width="140" sortable>
			<template #default="item">
				<el-tag v-if="item.row.concurrent == 0">是</el-tag>
				<el-tag v-if="item.row.concurrent == 1">否</el-tag>
			</template>
		</el-table-column>
		<el-table-column prop="status" label="状态" width="180" sortable>
			<template #default="item">
				<el-switch
					v-model="item.row.status"
					:active-value="0"
					:inactive-value="1"
					class="mb-2"
					active-text="正常"
					inactive-text="暂停"
					@change="handleChangeStatus(item.row)"
				/>
				<!-- <el-tag v-if="item.row.status == 0">正常</el-tag>
				<el-tag v-if="item.row.status == 1">暂停</el-tag> -->
			</template>
		</el-table-column>
		<el-table-column
			prop="remark"
			label="备注"
			width="300"
			show-overflow-tooltip
		></el-table-column>
		<el-table-column fixed="right" label="操作" min-width="120">
			<template #default="scope">
				<el-button
					link
					type="primary"
					size="small"
					@click="openModifyTimedTaskDialog(scope.$index, scope.row)"
					>修改</el-button
				>
				<el-button
					link
					type="primary"
					size="small"
					@click="handleDeleteTimedTask(scope.$index, scope.row)"
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
		:title="addOrModifyTag == 1 ? '添加任务' : '修改任务'"
		v-model="dialogFormAddOrModifyTimedTask"
		width="800px"
	>
		<el-form
			:model="addOrModifyTimedTaskForm"
			:rules="addOrModifyTimedTaskFormRules"
			ref="addOrModifyTimedTaskFormRef"
		>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item
						label="任务名称"
						:label-width="addOrModifyTimedTaskFormLabelWidth"
						prop="taskName"
						required
					>
						<el-input
							v-model="addOrModifyTimedTaskForm.taskName"
							placeholder="请输入任务名称"
							clearable
						/>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item
						label="任务分组"
						:label-width="addOrModifyTimedTaskFormLabelWidth"
						prop="taskGroup"
					>
						<el-select
							v-model="addOrModifyTimedTaskForm.taskGroup"
							placeholder="请选择任务分组"
							style="width: 100%"
						>
							<el-option label="默认任务" value="DEFAULT"></el-option>
							<el-option label="系统任务" value="SYSTEM"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-form-item
				:label-width="addOrModifyTimedTaskFormLabelWidth"
				prop="invokeTarget"
				required
			>
				<template #label>
					调用方法
					<el-tooltip placement="top">
						<template #content>
							Bean调用示例：ryTask.ryParams('ry')
							<br />Class类调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
							<br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
						</template>
						<el-icon :size="20" style="margin-top: 5px; margin-left: 5px"
							><i-ep-questionfilled
						/></el-icon>
					</el-tooltip>
				</template>
				<el-input
					v-model="addOrModifyTimedTaskForm.invokeTarget"
					placeholder="请输入调用目标字符串"
					clearable
				/>
			</el-form-item>
			<el-form-item
				label="cron表达式"
				:label-width="addOrModifyTimedTaskFormLabelWidth"
				prop="cronExpression"
				required
			>
				<el-input
					v-model="addOrModifyTimedTaskForm.cronExpression"
					placeholder="请输入cron执行表达式"
					clearable
					class="input-with-select"
				>
					<template #append>
						<el-button type="primary" @click="generateCronExpression"
							>生成表达式
						</el-button>
					</template>
				</el-input>
			</el-form-item>
			<el-row :gutter="10">
				<el-col :span="12">
					<el-form-item
						label="执行策略"
						:label-width="addOrModifyTimedTaskFormLabelWidth"
						prop="misfirePolicy"
					>
						<el-radio-group v-model="addOrModifyTimedTaskForm.misfirePolicy">
							<el-radio-button :label="1">立即执行</el-radio-button>
							<el-radio-button :label="2">执行一次</el-radio-button>
							<el-radio-button :label="3">放弃执行</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item
						label="是否并发"
						:label-width="addOrModifyTimedTaskFormLabelWidth"
						prop="concurrent"
					>
						<el-radio-group v-model="addOrModifyTimedTaskForm.concurrent">
							<el-radio-button :label="0">允许</el-radio-button>
							<el-radio-button :label="1">禁止</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="addOrModifyTimedTaskCancel">取 消</el-button>
				<el-button
					type="primary"
					@click="addOrModifyTimedTaskConfirm(addOrModifyTimedTaskFormRef)"
				>
					确 定
				</el-button>
			</span>
		</template>
	</el-dialog>

	<!-- Cron表达式生成器对话框 -->
	<el-dialog title="生成Cron表达式" v-model="openCron" width="800px">
		<crontab
			@hide="openCron = false"
			@fill="crontabFill"
			:expression="addOrModifyTimedTaskForm.cronExpression"
		></crontab>
	</el-dialog>
</template>

<script setup lang="ts" name="timedTask">
import { ref, onMounted } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import {
	getTimedTaskList,
	addTimedTask,
	modifyTimedTask,
	deleteTimedTask,
	modifyTimedTaskStatus,
} from "@/api/task/timedTask/index";
import { Timer } from "@element-plus/icons-vue";
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
//cron表达式相关
const cronExpression = ref("");
const openCron = ref(false);
/*============================通用参数结束============================*/

/*============================页面方法开始============================*/
//获取列表
const getList = async () => {
	let result = await getTimedTaskList(tableParams.value);
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
/*********添加修改定时任务*********/
//弹出框是否显示
let dialogFormAddOrModifyTimedTask = ref(false);
//表单元素宽度
let addOrModifyTimedTaskFormLabelWidth = ref("110px");
//添加定时任务表单初始值
const initialAddOrModifyTimedTaskForm = {
	taskId: 0,
	taskName: "",
	taskGroup: "DEFAULT",
	invokeTarget: "",
	cronExpression: "",
	misfirePolicy: 1, // 立即执行
	concurrent: 0, // 禁止并发
	status: 1, // 正常
};
//添加定时任务表单对象
const addOrModifyTimedTaskForm = ref({ ...initialAddOrModifyTimedTaskForm });
//添加定时任务表单对象
const addOrModifyTimedTaskFormRef = ref<FormInstance>();
//添加定时任务表单验证规则
const addOrModifyTimedTaskFormRules = reactive<FormRules>({
	taskName: [
		{ required: true, message: "请输入任务名称", trigger: "blur" },
		{ min: 2, max: 50, message: "长度在2到50个字符", trigger: "blur" },
	],
	invokeTarget: [
		{ required: true, message: "请输入调用目标字符串", trigger: "blur" },
		{ min: 5, max: 200, message: "长度在5到200个字符", trigger: "blur" },
	],
	cronExpression: [{ required: true, message: "请输入cron表达式", trigger: "blur" }],
});
//打开添加弹框
const openAddTimedTaskDialog = async () => {
	//弹出框设置为添加弹框
	addOrModifyTag.value = 1;
	//表单值恢复为初始值
	addOrModifyTimedTaskForm.value = { ...initialAddOrModifyTimedTaskForm };
	//清除验证状态
	addOrModifyTimedTaskFormRef.value?.clearValidate();
	//添加表单的主键设置为0
	addOrModifyTimedTaskForm.value.taskId = 0;
	//显示弹出框
	dialogFormAddOrModifyTimedTask.value = true;
};
//打开修改弹框
const openModifyTimedTaskDialog = async (index: number, row: any) => {
	//弹出框设置为修改弹框
	addOrModifyTag.value = 2;
	//表单值恢复为初始值
	addOrModifyTimedTaskForm.value = { ...initialAddOrModifyTimedTaskForm };
	//清除验证状态
	addOrModifyTimedTaskFormRef.value?.clearValidate();
	//显示弹出框
	dialogFormAddOrModifyTimedTask.value = true;
	//设置弹出框中相关值
	addOrModifyTimedTaskForm.value.taskId = row.taskId;
	addOrModifyTimedTaskForm.value.taskName = row.taskName;
	addOrModifyTimedTaskForm.value.taskGroup = row.taskGroup;
	addOrModifyTimedTaskForm.value.invokeTarget = row.invokeTarget;
	addOrModifyTimedTaskForm.value.cronExpression = row.cronExpression;
	addOrModifyTimedTaskForm.value.misfirePolicy = row.misfirePolicy;
	addOrModifyTimedTaskForm.value.concurrent = row.concurrent;
	addOrModifyTimedTaskForm.value.status = row.status;
};
//实现添加或修改
const addOrModifyTimedTaskConfirm = async (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	await formEl.validate(async (valid, fields) => {
		if (valid) {
			//调用添加或修改方法
			if (addOrModifyTag.value == 1) {
				let result = await addTimedTask(addOrModifyTimedTaskForm.value);
				if (result.code == 200) {
					ElMessage({
						message: "添加成功",
						type: "success",
					});
					//关闭弹框
					dialogFormAddOrModifyTimedTask.value = false;
					//表单值恢复为初始值
					addOrModifyTimedTaskForm.value = {
						...initialAddOrModifyTimedTaskForm,
					};
					//重新加载表单
					search();
				} else {
					ElMessage.error(result.msg);
				}
			} else {
				let result = await modifyTimedTask(addOrModifyTimedTaskForm.value);
				if (result.code == 200) {
					ElMessage({
						message: "修改成功",
						type: "success",
					});
					//关闭弹框
					dialogFormAddOrModifyTimedTask.value = false;
					//表单值恢复为初始值
					addOrModifyTimedTaskForm.value = {
						...initialAddOrModifyTimedTaskForm,
					};
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
const addOrModifyTimedTaskCancel = async () => {
	//关闭弹框
	dialogFormAddOrModifyTimedTask.value = false;
	//表单值恢复为初始值
	addOrModifyTimedTaskForm.value = { ...initialAddOrModifyTimedTaskForm };
};
// 打开Cron表达式生成器
const generateCronExpression = () => {
	openCron.value = true;
};
//回显
const crontabFill = (value: string) => {
	addOrModifyTimedTaskForm.value.cronExpression = value;
};
/*********删除定时任务*********/
const handleDeleteTimedTask = async (index: number, row: any) => {
	//调用删除方法
	try {
		let result = await deleteTimedTask(row.taskId);
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
// 处理状态切换
const handleChangeStatus = async (row: any) => {
	// 调用修改方法
	try {
		let result = await modifyTimedTaskStatus({
			taskId: row.taskId,
			status: row.status,
		});
		if (result.code == 200) {
			ElMessage({
				message: "修改成功",
				type: "success",
			});
		} else {
			ElMessage.error(result.msg);
		}
	} catch (error) {
		ElMessage.error("修改失败");
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
