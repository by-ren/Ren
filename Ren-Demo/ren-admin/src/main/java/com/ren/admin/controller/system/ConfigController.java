package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.page.TableDataInfo;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.interfaces.Pageable;
import com.ren.system.entity.Config;
import com.ren.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/config")
@Tag(name = "系统配置相关", description = "系统配置相关")
public class ConfigController extends BaseController {

    @Autowired
    ConfigService configService;

    /*
     * 配置分页列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启分页，请添加该注解
    @Operation(summary = "配置分页列表", description = "配置分页列表")
    public TableDataInfo listConfigByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Config> configList = configService.listConfigByPage(paramMap);
        return getDataTable(configList);
    }

    /*
     * 添加配置
     * @param loginUser
     * @param addConfig
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "配置模块", businessType = BusinessType.INSERT)
    @Operation(summary = "添加配置", description = "添加配置")
    public AjaxResult addConfig(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Config addConfig) {
        configService.addConfig(addConfig,loginUser.getUsername());
        return success();
    }

    /*
     * 编辑配置
     * @param loginUser
     * @param modifyConfig
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "配置模块", businessType = BusinessType.UPDATE)
    @Operation(summary = "编辑配置", description = "编辑配置")
    public AjaxResult modifyConfig(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Config modifyConfig) {
        configService.modifyConfig(modifyConfig,loginUser.getUsername());
        return success();
    }

    /*
     * 删除配置
     * @param loginUser
     * @param configId
     * @return com.ren.common.domain.dto.AjaxResult
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "配置模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除配置", description = "删除配置")
    public AjaxResult configDelete(@AuthenticationPrincipal LoginUser loginUser, long configId) {
        configService.removeConfig(configId);
        return success();
    }

}