
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.system.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 添加岗位
     * @param post
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    void insertPost(Post post);

    /**
     * 删除岗位
     * @param postId
     * @author ren
     * @date 2025/05/18 13:49
     */
    void deletePost(long postId);

    /**
     * 编辑岗位
     * @param post
     * @author ren
     * @date 2025/05/18 13:49
     */
    void updatePost(Post post);

    /**
     * 分页获取岗位列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Post>
     * @author ren
     * @date 2025/05/18 13:50
     */
    IPage<Post> listPostByPage(Page<Post> page, @Param("paramMap")Map<String, Object> paramMap);

    /**
     * 岗位列表
     * @param paramMap
     * @return java.util.List<com.ren.system.entity.Post>
     * @author ren
     * @date 2025/05/23 14:30
     */
    List<Post> listPostByParam(Map<String, Object> paramMap);
}