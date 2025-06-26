import { service } from "@/axios/axios";

//获取字典数据列表
export const getDictDataList = (params?: Record<string, any>) => {
	return service({
		method: "get",
		url: "/dictData/list/page",
		params: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//添加字典数据
export const addDictData = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/dictData/add",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//修改字典数据
export const modifyDictData = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/dictData/modify",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//删除字典数据
export const deleteDictData = (dictDataId: number) => {
	return service({
		method: "delete",
		url: "/dictData/delete",
		params: { dictDataId }, // 通过 URL 参数传递（如 /dictData/delete?dictDataId=1）
	}).then((response) => response.data);
};

//获取字典类型列表
export const getDictTypeList = () => {
	return service({
		method: "get",
		url: `/dictType/list`,
	}).then((response) => response.data);
};
