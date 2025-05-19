import { service } from '@/axios/axios'

//获取岗位列表
export const getPostList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/post/list/page',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//添加岗位
export const addPost = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/post/add',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//修改岗位
export const modifyPost = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/post/modify',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//删除岗位
export const deletePost = (postId:number) => {
  return service({
    method: 'delete',
    url: '/post/delete',
    params: { postId } // 通过 URL 参数传递（如 /post/delete?postId=1）
  }).then(response => response.data);
};