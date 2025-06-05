import { service } from '@/axios/axios'

//获取在线用户列表
export const getSysUserOnlineList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/monitor/online/list',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//强退在线用户
export const compulsoryWithdrawalSysUserOnline = (tokenId:string) => {
  return service({
    method: 'get',
    url: '/monitor/online/compulsoryWithdrawal',
    params: { tokenId } // 通过 URL 参数传递（如 /monitor/online/compulsoryWithdrawal?tokenId=1）
  }).then(response => response.data);
};