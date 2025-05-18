
package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.Post;

import java.util.Map;

public interface PostService extends IService<Post> {

    /*
     * 添加岗位
     * @param post
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    long addPost(Post post,String createBy);

    /*
     * 删除岗位
     * @param postId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void removePost(long postId);

    /*
     * 编辑岗位
     * @param post
     * @author admin
     * @date 2025/05/18 13:49
     */
    void modifyPost(Post post,String updateBy);

    /*
     * 分页获取岗位列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Post>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<Post> listPostByPage(Map<String,Object> paramMap);

    /*
     * 获取岗位详情
     * @param postId
     * @return com.ren.system.entity.Post
     * @author admin
     * @date 2025/05/18 13:50
     */
    Post getPostById(long postId);
}