import { service } from '@/utils/axios'

export const getUserList = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/user/list',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
}