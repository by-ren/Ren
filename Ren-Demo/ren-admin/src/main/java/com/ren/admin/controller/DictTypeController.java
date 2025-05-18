package com.ren.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.page.TableDataInfo;
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
     * @author admin
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
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    public AjaxResultDTO addConfig(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Config addConfig) {
        configService.addConfig(addConfig,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑配置
     * @param loginUser
     * @param modifyConfig
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    public AjaxResultDTO modifyConfig(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Config modifyConfig) {
        configService.modifyConfig(modifyConfig,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除配置
     * @param loginUser
     * @param configId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    public AjaxResultDTO configDelete(@AuthenticationPrincipal User loginUser, long configId) {
        configService.removeConfig(configId);
        return AjaxResultDTO.success();
    }

}