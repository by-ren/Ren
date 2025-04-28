import { service } from '@/utils/axios'

export const getUserList = () => {
    return service({
      method: 'post',
      url: '/user/list'
    }).then(response => response.data)
  }