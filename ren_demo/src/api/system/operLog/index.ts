import { service } from "@/axios/axios";

//获取操作日志列表
export const getOperLogList = (params?: Record<string, any>) => {
	return service({
		method: "get",
		url: "/operLog/list/page",
		params: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//删除操作日志
export const deleteOperLog = (operLogId: number) => {
	return service({
		method: "delete",
		url: "/operLog/delete",
		params: { operLogId }, // 通过 URL 参数传递（如 /operLog/delete?operLogId=1）
	}).then((response) => response.data);
};
