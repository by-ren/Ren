package com.ren.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.Pageable;
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
     * @author admin
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
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    public AjaxResultDTO addNotice(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Notice addNotice) {
        noticeService.addNotice(addNotice,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑通知公告
     * @param loginUser
     * @param modifyNotice
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    public AjaxResultDTO modifyNotice(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Notice modifyNotice) {
        noticeService.modifyNotice(modifyNotice,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除通知公告
     * @param loginUser
     * @param noticeId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    public AjaxResultDTO noticeDelete(@AuthenticationPrincipal User loginUser, long noticeId) {
        noticeService.removeNotice(noticeId);
        return AjaxResultDTO.success();
    }

}