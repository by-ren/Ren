import { service } from '@/axios/axios'

//获取在线用户列表
export const getSeverInfo = () => {
  return service({
    method: 'get',
    url: '/monitor/server/view',
  }).then(response => response.data)
}