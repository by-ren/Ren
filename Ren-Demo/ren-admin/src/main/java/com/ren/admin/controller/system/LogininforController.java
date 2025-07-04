package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.page.TableDataInfo;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.interfaces.Pageable;
import com.ren.common.core.domain.entity.Logininfor;
import com.ren.system.service.LogininforService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/logininfor")
@Tag(name = "登录日志相关", description = "登录日志相关")
public class LogininforController extends BaseController {

    @Autowired
    LogininforService logininforService;

    /*
     * 登录日志分页列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启分页，请添加该注解
    @Operation(summary = "登录日志分页列表", description = "登录日志分页列表")
    public TableDataInfo listLogininforByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Logininfor> logininforList = logininforService.listLogininforByPage(paramMap);
        return getDataTable(logininforList);
    }

    /*
     * 删除登录日志
     * @param loginUser
     * @param logininforId
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "日志模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除登录日志", description = "删除登录日志")
    public AjaxResult logininforDelete(@AuthenticationPrincipal LoginUser loginUser, long logininforId) {
        logininforService.removeLogininfor(logininforId);
        return success();
    }

}