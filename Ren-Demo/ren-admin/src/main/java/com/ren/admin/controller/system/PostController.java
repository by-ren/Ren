package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.page.TableDataInfo;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.interfaces.Pageable;
import com.ren.system.entity.Post;
import com.ren.system.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@Tag(name = "岗位相关", description = "岗位相关")
public class PostController extends BaseController {

    @Autowired
    PostService postService;

    /*
     * 岗位分页列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启岗位，请添加该注解
    @Operation(summary = "岗位分页列表", description = "岗位分页列表")
    public TableDataInfo listPostByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Post> postList = postService.listPostByPage(paramMap);
        return getDataTable(postList);
    }

    /*
     * 岗位列表
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/23 14:29
     */
    @GetMapping("/list")
    @Operation(summary = "岗位列表", description = "岗位列表")
    public AjaxResult listPost() {
        List<Post> postList = postService.listPostByParam(null);
        return success().put("postList",postList);
    }

    /*
     * 添加岗位
     * @param loginUser
     * @param addPost
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "岗位模块", businessType = BusinessType.INSERT)
    @Operation(summary = "添加岗位", description = "添加岗位")
    public AjaxResult addPost(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Post addPost) {
        postService.addPost(addPost,loginUser.getUsername());
        return success();
    }

    /*
     * 编辑岗位
     * @param loginUser
     * @param modifyPost
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "岗位模块", businessType = BusinessType.UPDATE)
    @Operation(summary = "编辑岗位", description = "编辑岗位")
    public AjaxResult modifyPost(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Post modifyPost) {
        postService.modifyPost(modifyPost,loginUser.getUsername());
        return success();
    }

    /*
     * 删除岗位
     * @param loginUser
     * @param postId
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "岗位模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除岗位", description = "删除岗位")
    public AjaxResult postDelete(@AuthenticationPrincipal LoginUser loginUser, long postId) {
        postService.removePost(postId);
        return success();
    }

}