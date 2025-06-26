import { service } from "@/axios/axios";

//获取缓存列表
export const listCache = () => {
	return service({
		method: "get",
		url: "/monitor/cache/list",
	}).then((response) => response.data);
};

//缓存键值列表
export const listKey = (name: string) => {
	return service({
		method: "get",
		url: "/monitor/cache/key/list",
		params: { name },
	}).then((response) => response.data);
};

//删除缓存
export const deleteCache = (name: string) => {
	return service({
		method: "delete",
		url: "/monitor/cache/delete",
		params: { name }, // 通过 URL 参数传递（如 /dept/delete?deptId=1）
	}).then((response) => response.data);
};

//删除键值
export const deleteKey = (name: string, key: string) => {
	return service({
		method: "delete",
		url: "/monitor/cache/key/delete",
		params: { name, key }, // 通过 URL 参数传递（如 /dept/delete?deptId=1）
	}).then((response) => response.data);
};

//清空键值
export const cleanCacheList = () => {
	return service({
		method: "delete",
		url: "/monitor/cache/clean",
	}).then((response) => response.data);
};
