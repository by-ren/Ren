package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.bo.LoginUser;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.interfaces.Pageable;
import com.ren.framework.manager.AsyncManager;
import com.ren.framework.manager.factory.AsyncFactory;
import com.ren.system.entity.Notice;
import com.ren.system.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notice")
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
    public TableDataInfo listNoticeByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Notice> noticeList = noticeService.listNoticeByPage(paramMap);
        return getDataTable(noticeList);
    }

    /*
     * 添加通知公告
     * @param loginUser
     * @param addNotice
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "通知公告模块", businessType = BusinessType.INSERT)
    public AjaxResultDTO addNotice(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Notice addNotice) {
        noticeService.addNotice(addNotice,loginUser.getUsername());
        //线程池异步发送邮件
        AsyncManager.me().execute(AsyncFactory.sendAddOrModifyNoticeEmail(addNotice,"Ren系统通知公告新增"));
        return AjaxResultDTO.success();
    }

    /*
     * 编辑通知公告
     * @param loginUser
     * @param modifyNotice
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "通知公告模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO modifyNotice(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Notice modifyNotice) {
        noticeService.modifyNotice(modifyNotice,loginUser.getUsername());
        //线程池异步发送邮件
        AsyncManager.me().execute(AsyncFactory.sendAddOrModifyNoticeEmail(modifyNotice,"Ren系统通知公告变更"));
        return AjaxResultDTO.success();
    }

    /*
     * 删除通知公告
     * @param loginUser
     * @param noticeId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "通知公告模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO noticeDelete(@AuthenticationPrincipal LoginUser loginUser, long noticeId) {
        noticeService.removeNotice(noticeId);
        return AjaxResultDTO.success();
    }

}