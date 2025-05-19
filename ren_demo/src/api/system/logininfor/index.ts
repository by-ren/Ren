import { service } from '@/axios/axios'

//获取操作日志列表
export const getLogininforList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/logininfor/list/page',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//删除操作日志
export const deleteLogininfor = (logininforId:number) => {
  return service({
    method: 'delete',
    url: '/logininfor/delete',
    params: { logininforId } // 通过 URL 参数传递（如 /logininfor/delete?logininforId=1）
  }).then(response => response.data);
};