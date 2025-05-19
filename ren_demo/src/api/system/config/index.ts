import { service } from '@/axios/axios'

//获取参数配置列表
export const getConfigList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/config/list/page',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//添加参数配置
export const addConfig = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/config/add',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//修改参数配置
export const modifyConfig = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/config/modify',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//删除参数配置
export const deleteConfig = (configId:number) => {
  return service({
    method: 'delete',
    url: '/config/delete',
    params: { configId } // 通过 URL 参数传递（如 /config/delete?configId=1）
  }).then(response => response.data);
};