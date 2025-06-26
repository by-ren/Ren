import { service } from "@/axios/axios";

//获取角色列表
export const getRoleList = (params?: Record<string, any>) => {
	return service({
		method: "get",
		url: "/role/list/page",
		params: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//添加角色
export const addRole = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/role/add",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//修改角色
export const modifyRole = (params?: Record<string, any>) => {
	return service({
		method: "post",
		url: "/role/modify",
		data: params || {}, // 无参数时传空对象
	}).then((response) => response.data);
};

//删除角色
export const deleteRole = (roleId: number) => {
	return service({
		method: "delete",
		url: "/role/delete",
		params: { roleId }, // 通过 URL 参数传递（如 /role/delete?roleId=1）
	}).then((response) => response.data);
};

//获取角色菜单列表
export const getMenuList = (roleId?: number) => {
	return service({
		method: "get",
		url: `/menu/list/role`,
		params: { roleId },
	}).then((response) => response.data);
};

//获取角色部门列表
export const getDeptList = (roleId?: number) => {
	return service({
		method: "get",
		url: `/dept/list/role`,
		params: { roleId },
	}).then((response) => response.data);
};
