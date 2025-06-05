package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.bo.LoginUser;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.interfaces.Pageable;
import com.ren.system.entity.Config;
import com.ren.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/config")
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
    public TableDataInfo listConfigByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Config> configList = configService.listConfigByPage(paramMap);
        return getDataTable(configList);
    }

    /*
     * 添加配置
     * @param loginUser
     * @param addConfig
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "配置模块", businessType = BusinessType.INSERT)
    public AjaxResultDTO addConfig(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Config addConfig) {
        configService.addConfig(addConfig,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑配置
     * @param loginUser
     * @param modifyConfig
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "配置模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO modifyConfig(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Config modifyConfig) {
        configService.modifyConfig(modifyConfig,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除配置
     * @param loginUser
     * @param configId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "配置模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO configDelete(@AuthenticationPrincipal LoginUser loginUser, long configId) {
        configService.removeConfig(configId);
        return AjaxResultDTO.success();
    }

}