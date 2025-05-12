import { service } from '@/utils/axios'

//获取菜单列表
export const getMenuList = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/menu/list',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
}

//添加菜单
export const addMenu = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/menu/add',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//修改菜单
export const modifyMenu = (params? : Record<string, any>) => {
  return service({
    method: 'post',
    url: '/menu/modify',
    data: params || {} // 无参数时传空对象
  }).then(response => response.data)
};

//删除菜单
export const deleteMenu = (menuId:number) => {
  return service({
    method: 'delete',
    url: '/menu/delete',
    params: { menuId } // 通过 URL 参数传递（如 /menu/delete?menuId=1）
  }).then(response => response.data);
};

//获取上级菜单列表
export const getParentMenuList = (menuId: number = -1) => {
  return service({
    method: 'get',
    url: `/menu/list/parent/${menuId}`,
  }).then(response => response.data)
}