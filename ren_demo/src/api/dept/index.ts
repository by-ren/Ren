import { service } from '@/axios/axios'

//获取部门列表
export const getDeptList = (params? : Record<string, any>) => {
  return service({
    method: 'get',
    url: '/dept/list/tree',
    params: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//添加部门
export const addDept = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/dept/add',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//修改部门
export const modifyDept = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/dept/modify',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//删除部门
export const deleteDept = (deptId:number) => {
  return service({
    method: 'delete',
    url: '/dept/delete',
    params: { deptId } // 通过 URL 参数传递（如 /dept/delete?deptId=1）
  }).then(response => response.data);
};

//获取上级部门列表
export const getParentDeptList = (deptId: number = -1) => {
  return service({
    method: 'get',
    url: `/dept/list/parent/${deptId}`,
  }).then(response => response.data)
}