
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.system.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

	/*
	 * 添加用户角色
	 * @param userRole
	 * @author admin
	 * @date 2025/05/07 17:17
	 */
	void insertUserRole(UserRole userRole);

	/*
	 * 删除用户角色
	 * @param userId
	 * @author admin
	 * @date 2025/05/07 17:18
	 */
	void deleteUserRoleByUserId(long userId);

	/*
	 * 获取用户角色列表
	 * @param userId
	 * @return java.util.List<com.ren.system.entity.UserRole>
	 * @author admin
	 * @date 2025/05/07 17:19
	 */
	List<UserRole> listUserRoleByUserId(long userId);
}