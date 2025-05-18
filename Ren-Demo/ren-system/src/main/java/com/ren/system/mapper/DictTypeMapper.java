
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.system.entity.DictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface DictTypeMapper extends BaseMapper<DictType> {

    /*
     * 添加字典类型
     * @param dictType
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    void insertDictType(DictType dictType);

    /*
     * 删除字典类型
     * @param dictTypeId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void deleteDictType(long dictTypeId);

    /*
     * 编辑字典类型
     * @param dictType
     * @author admin
     * @date 2025/05/18 13:49
     */
    void updateDictType(DictType dictType);

    /*
     * 分页获取字典类型列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.DictType>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<DictType> listDictTypeByPage(Page<DictType> page, @Param("paramMap")Map<String, Object> paramMap);

}