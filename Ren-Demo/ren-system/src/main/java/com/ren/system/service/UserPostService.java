
package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.UserPost;

import java.util.List;

public interface UserPostService extends IService<UserPost> {

    /*
     * 添加用户岗位
     * @param userPost
     * @author admin
     * @date 2025/05/07 17:17
     */
    void addUserPost(UserPost userPost);

    /*
     * 批量添加用户岗位
     * @param userPostList
     * @author admin
     * @date 2025/05/23 14:47
     */
    void addUserPostBatch(List<UserPost> userPostList);

    /*
     * 删除用户岗位
     * @param userId
     * @author admin
     * @date 2025/05/07 17:18
     */
    void removeUserPostByUserId(long userId);

    /*
     * 获取用户岗位列表
     * @param userId
     * @return java.util.List<com.ren.system.entity.UserPost>
     * @author admin
     * @date 2025/05/07 17:19
     */
    List<UserPost> listUserPostByUserId(long userId);

}