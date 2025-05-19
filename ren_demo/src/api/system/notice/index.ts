import { service } from '@/axios/axios'

//获取通知公告列表
export const getNoticeList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/notice/list/page',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//添加通知公告
export const addNotice = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/notice/add',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//修改通知公告
export const modifyNotice = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/notice/modify',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//删除通知公告
export const deleteNotice = (noticeId:number) => {
  return service({
    method: 'delete',
    url: '/notice/delete',
    params: { noticeId } // 通过 URL 参数传递（如 /notice/delete?noticeId=1）
  }).then(response => response.data);
};