package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.bo.LoginUser;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.interfaces.Pageable;
import com.ren.system.entity.DictType;
import com.ren.system.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dictType")
public class DictTypeController extends BaseController {

    @Autowired
    DictTypeService dictTypeService;

    /*
     * 字典类型字典类型列表
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/18 15:28
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启字典类型，请添加该注解
    public TableDataInfo listDictTypeByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<DictType> dictTypeList = dictTypeService.listDictTypeByPage(paramMap);
        return getDataTable(dictTypeList);
    }

    /*
     * 字典类型列表
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/23 13:51
     */
    @GetMapping("/list")
    public AjaxResultDTO listDictType() {
        List<DictType> dictTypeList = dictTypeService.listDictTypeByParam(null);
        return AjaxResultDTO.success().put("dictTypeList",dictTypeList);
    }

    /*
     * 添加字典类型
     * @param loginUser
     * @param addDictType
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/add")
    @OperLogAnn(title = "字典模块", businessType = BusinessType.INSERT)
    public AjaxResultDTO addDictType(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) DictType addDictType) {
        dictTypeService.addDictType(addDictType,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑字典类型
     * @param loginUser
     * @param modifyDictType
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "字典模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO modifyDictType(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) DictType modifyDictType) {
        dictTypeService.modifyDictType(modifyDictType,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除字典类型
     * @param loginUser
     * @param dictTypeId
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/18 15:28
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "字典模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO dictTypeDelete(@AuthenticationPrincipal LoginUser loginUser, long dictTypeId) {
        dictTypeService.removeDictType(dictTypeId);
        return AjaxResultDTO.success();
    }

}