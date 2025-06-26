
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.system.entity.UserPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserPostMapper extends BaseMapper<UserPost> {

	/**
	 * 添加用户岗位
	 * @param userPost
	 * @author ren
	 * @date 2025/05/07 17:17
	 */
	void insertUserPost(UserPost userPost);

	/**
	 * 批量添加用户岗位
	 * @param userPostList
	 * @author ren
	 * @date 2025/05/12 19:40
	 */
	void insertUserPostBatch(List<UserPost> userPostList);

	/**
	 * 删除用户岗位
	 * @param userId
	 * @author ren
	 * @date 2025/05/07 17:18
	 */
	void deleteUserPostByUserId(long userId);

	/**
	 * 获取用户岗位列表
	 * @param userId
	 * @return java.util.List<com.ren.system.entity.UserPost>
	 * @author ren
	 * @date 2025/05/07 17:19
	 */
	List<UserPost> listUserPostByUserId(long userId);

}