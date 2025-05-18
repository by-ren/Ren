
package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.DictType;

import java.util.Map;

public interface DictTypeService extends IService<DictType> {

    /*
     * 添加字典类型
     * @param dictType
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    long addDictType(DictType dictType,String createBy);

    /*
     * 删除字典类型
     * @param dictTypeId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void removeDictType(long dictTypeId);

    /*
     * 编辑字典类型
     * @param dictType
     * @author admin
     * @date 2025/05/18 13:49
     */
    void modifyDictType(DictType dictType,String updateBy);

    /*
     * 分页获取字典类型列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.DictType>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<DictType> listDictTypeByPage(Map<String,Object> paramMap);

    /*
     * 获取字典类型详情
     * @param dictTypeId
     * @return com.ren.system.entity.DictType
     * @author admin
     * @date 2025/05/18 13:50
     */
    DictType getDictTypeById(long dictTypeId);
}