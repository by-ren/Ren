
package com.ren.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.system.entity.UserRole;
import com.ren.system.mapper.UserRoleMapper;
import com.ren.system.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;

	/**
	 * 添加用户角色
	 * @param userRole
	 * @author ren
	 * @date 2025/05/07 17:17
	 */
	@Override
	public void addUserRole(UserRole userRole) {
		userRoleMapper.insertUserRole(userRole);
	}

	/**
	 * 批量添加用户角色
	 * @param userRoleList
	 * @author ren
	 * @date 2025/05/23 14:45
	 */
	@Override
	public void addUserRoleBatch(List<UserRole> userRoleList) {
		userRoleMapper.insertUserRoleBatch(userRoleList);
	}

	/**
	 * 删除用户角色
	 * @param userId
	 * @author ren
	 * @date 2025/05/07 17:18
	 */
	@Override
	public void removeUserRoleByUserId(long userId) {
		userRoleMapper.deleteUserRoleByUserId(userId);
	}

	/**
	 * 获取用户角色列表
	 * @param userId
	 * @return java.util.List<com.ren.system.entity.UserRole>
	 * @author ren
	 * @date 2025/05/07 17:19
	 */
	@Override
	public List<UserRole> listUserRoleByUserId(long userId) {
		List<UserRole> userRoleList = userRoleMapper.listUserRoleByUserId(userId);
		return userRoleList;
	}
}