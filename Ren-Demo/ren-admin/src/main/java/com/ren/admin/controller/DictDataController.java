package com.ren.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.Pageable;
import com.ren.system.entity.DictData;
import com.ren.system.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dictData")
public class DictDataController extends BaseController {

    @Autowired
    DictDataService dictDataService;

    /*
     * 字典数据字典数据列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author admin
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启字典数据，请添加该注解
    public TableDataInfo listDictDataByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<DictData> dictDataList = dictDataService.listDictDataByPage(paramMap);
        return getDataTable(dictDataList);
    }

    /*
     * 添加字典数据
     * @param loginUser
     * @param addDictData
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    public AjaxResultDTO addDictData(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) DictData addDictData) {
        dictDataService.addDictData(addDictData,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑字典数据
     * @param loginUser
     * @param modifyDictData
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    public AjaxResultDTO modifyDictData(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) DictData modifyDictData) {
        dictDataService.modifyDictData(modifyDictData,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除字典数据
     * @param loginUser
     * @param dictDataId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    public AjaxResultDTO dictDataDelete(@AuthenticationPrincipal User loginUser, long dictDataId) {
        dictDataService.removeDictData(dictDataId);
        return AjaxResultDTO.success();
    }

}