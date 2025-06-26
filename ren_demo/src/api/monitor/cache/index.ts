import { service } from "@/axios/axios";

//获取缓存列表
export const getCacheInfo = () => {
	return service({
		method: "get",
		url: "/monitor/cache/detail",
	}).then((response) => response.data);
};
