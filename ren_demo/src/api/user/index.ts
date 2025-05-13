import { service } from '@/utils/axios'

//获取用户列表
export const getUserList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/user/list/page',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//添加用户
export const addUser = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/user/add',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//修改用户
export const modifyUser = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/user/modify',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//删除用户
export const deleteUser = (userId:number) => {
  return service({
    method: 'delete',
    url: '/user/delete',
    params: { userId } // 通过 URL 参数传递（如 /user/delete?userId=1）
  }).then(response => response.data);
};

//重置密码
export const resetPassword = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/user/resetPassword',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//获取部门列表
export const getDeptList = () => {
  return service({
    method: 'get',
    url: `/dept/list`,
  }).then(response => response.data)
}

//获取角色列表
export const getRoleList = () => {
  return service({
    method: 'get',
    url: `/role/list`,
  }).then(response => response.data)
}

//获取角色信息
export const getUserInfo = (userId:number) => {
  return service({
    method: 'get',
    url: `/user/info/v2`,
    params: { userId },
  }).then(response => response.data)
}