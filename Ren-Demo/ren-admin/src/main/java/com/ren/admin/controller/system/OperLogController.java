package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.bo.LoginUser;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.interfaces.Pageable;
import com.ren.common.domain.entity.OperLog;
import com.ren.system.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/operLog")
public class OperLogController extends BaseController {

    @Autowired
    OperLogService operLogService;

    /*
     * 操作日志分页列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启分页，请添加该注解
    public TableDataInfo listOperLogByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<OperLog> operLogList = operLogService.listOperLogByPage(paramMap);
        return getDataTable(operLogList);
    }

    /*
     * 删除操作日志
     * @param loginUser
     * @param operLogId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "日志模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO operLogDelete(@AuthenticationPrincipal LoginUser loginUser, long operLogId) {
        operLogService.removeOperLog(operLogId);
        return AjaxResultDTO.success();
    }

}