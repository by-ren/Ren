
package com.ren.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.system.entity.UserPost;
import com.ren.system.mapper.UserPostMapper;
import com.ren.system.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPostServiceImpl extends ServiceImpl<UserPostMapper, UserPost> implements UserPostService {

	@Autowired
	private UserPostMapper userPostMapper;

	/*
	 * 添加用户岗位
	 * @param userPost
	 * @author admin
	 * @date 2025/05/07 17:17
	 */
	@Override
	public void addUserPost(UserPost userPost) {
		userPostMapper.insertUserPost(userPost);
	}

	/*
	 * 批量添加用户岗位
	 * @param userPostList
	 * @author admin
	 * @date 2025/05/23 14:47
	 */
	@Override
	public void addUserPostBatch(List<UserPost> userPostList) {
		userPostMapper.insertUserPostBatch(userPostList);
	}

	/*
	 * 删除用户岗位
	 * @param userId
	 * @author admin
	 * @date 2025/05/07 17:18
	 */
	@Override
	public void removeUserPostByUserId(long userId) {
		userPostMapper.deleteUserPostByUserId(userId);
	}

	/*
	 * 获取用户岗位列表
	 * @param userId
	 * @return java.util.List<com.ren.system.entity.UserPost>
	 * @author admin
	 * @date 2025/05/07 17:19
	 */
	@Override
	public List<UserPost> listUserPostByUserId(long userId) {
		List<UserPost> userPostList = userPostMapper.listUserPostByUserId(userId);
		return userPostList;
	}
}