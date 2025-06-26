import { service } from "@/axios/axios";

//获取定时任务列表
export const getTimedTaskList = (params?: Record<string, any>) => {
	return service({
		method: "get",
		url: "/timedTask/list/page",
		params: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//添加定时任务
export const addTimedTask = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/timedTask/add",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//修改定时任务
export const modifyTimedTask = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/timedTask/modify",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//修改定时任务状态
export const modifyTimedTaskStatus = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/timedTask/modifyStatus",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//删除定时任务
export const deleteTimedTask = (taskId: number) => {
	return service({
		method: "delete",
		url: "/timedTask/delete",
		params: { taskId }, // 通过 URL 参数传递（如 /timedTask/delete?taskId=1）
	}).then((response) => response.data);
};
