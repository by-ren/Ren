package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.page.TableDataInfo;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.interfaces.Pageable;
import com.ren.framework.manager.AsyncManager;
import com.ren.framework.factory.AsyncFactory;
import com.ren.system.entity.Notice;
import com.ren.system.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notice")
@Tag(name = "通知相关", description = "通知相关")
public class NoticeController extends BaseController {

    @Autowired
    NoticeService noticeService;

    /*
     * 通知公告字典类型列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启字典类型，请添加该注解
    @Operation(summary = "通知公告字典类型列表", description = "通知公告字典类型列表")
    public TableDataInfo listNoticeByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Notice> noticeList = noticeService.listNoticeByPage(paramMap);
        return getDataTable(noticeList);
    }

    /*
     * 添加通知公告
     * @param loginUser
     * @param addNotice
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "通知公告模块", businessType = BusinessType.INSERT)
    @Operation(summary = "添加通知公告", description = "添加通知公告")
    public AjaxResult addNotice(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Notice addNotice) {
        noticeService.addNotice(addNotice,loginUser.getUsername());
        //线程池异步发送邮件
        AsyncManager.me().execute(AsyncFactory.sendAddOrModifyNoticeEmail(addNotice,"Ren系统通知公告新增"));
        return success();
    }

    /*
     * 编辑通知公告
     * @param loginUser
     * @param modifyNotice
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "通知公告模块", businessType = BusinessType.UPDATE)
    @Operation(summary = "编辑通知公告", description = "编辑通知公告")
    public AjaxResult modifyNotice(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Notice modifyNotice) {
        noticeService.modifyNotice(modifyNotice,loginUser.getUsername());
        //线程池异步发送邮件
        AsyncManager.me().execute(AsyncFactory.sendAddOrModifyNoticeEmail(modifyNotice,"Ren系统通知公告变更"));
        return success();
    }

    /*
     * 删除通知公告
     * @param loginUser
     * @param noticeId
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "通知公告模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除通知公告", description = "删除通知公告")
    public AjaxResult noticeDelete(@AuthenticationPrincipal LoginUser loginUser, long noticeId) {
        noticeService.removeNotice(noticeId);
        return success();
    }

}