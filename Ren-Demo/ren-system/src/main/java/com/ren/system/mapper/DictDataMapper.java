
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.system.entity.DictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface DictDataMapper extends BaseMapper<DictData> {

    /*
     * 添加字典数据
     * @param dictData
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    void insertDictData(DictData dictData);

    /*
     * 删除字典数据
     * @param dictDataId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void deleteDictData(long dictDataId);

    /*
     * 编辑字典数据
     * @param dictData
     * @author admin
     * @date 2025/05/18 13:49
     */
    void updateDictData(DictData dictData);

    /*
     * 分页获取字典数据列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.DictData>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<DictData> listDictDataByPage(Page<DictData> page, @Param("paramMap")Map<String, Object> paramMap);

}