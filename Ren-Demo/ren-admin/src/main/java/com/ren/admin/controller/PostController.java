package com.ren.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.bo.LoginUser;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.interfaces.Pageable;
import com.ren.system.entity.Post;
import com.ren.system.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    PostService postService;

    /*
     * 岗位岗位列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author admin
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启岗位，请添加该注解
    public TableDataInfo listPostByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Post> postList = postService.listPostByPage(paramMap);
        return getDataTable(postList);
    }

    /*
     * 添加岗位
     * @param loginUser
     * @param addPost
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "岗位模块", businessType = BusinessType.INSERT)
    public AjaxResultDTO addPost(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Post addPost) {
        postService.addPost(addPost,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑岗位
     * @param loginUser
     * @param modifyPost
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "岗位模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO modifyPost(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Post modifyPost) {
        postService.modifyPost(modifyPost,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除岗位
     * @param loginUser
     * @param postId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "岗位模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO postDelete(@AuthenticationPrincipal LoginUser loginUser, long postId) {
        postService.removePost(postId);
        return AjaxResultDTO.success();
    }

}