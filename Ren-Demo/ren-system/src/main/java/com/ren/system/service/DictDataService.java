
package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.DictData;

import java.util.Map;

public interface DictDataService extends IService<DictData> {

    /*
     * 添加字典数据
     * @param dictData
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    long addDictData(DictData dictData,String createBy);

    /*
     * 删除字典数据
     * @param dictDataId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void removeDictData(long dictDataId);

    /*
     * 编辑字典数据
     * @param dictData
     * @author admin
     * @date 2025/05/18 13:49
     */
    void modifyDictData(DictData dictData,String updateBy);

    /*
     * 分页获取字典数据列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.DictData>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<DictData> listDictDataByPage(Map<String,Object> paramMap);

    /*
     * 获取字典数据详情
     * @param dictDataId
     * @return com.ren.system.entity.DictData
     * @author admin
     * @date 2025/05/18 13:50
     */
    DictData getDictDataById(long dictDataId);
}