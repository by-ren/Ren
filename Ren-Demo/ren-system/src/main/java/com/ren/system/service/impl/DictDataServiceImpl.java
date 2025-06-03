package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.utils.PageUtils;
import com.ren.system.entity.DictData;
import com.ren.system.mapper.DictDataMapper;
import com.ren.system.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 添加字典数据
     * @param dictData
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public long addDictData(DictData dictData,String createBy) {
        dictData.setCreateBy(createBy);
        dictData.setCreateTime(DateUtil.currentSeconds());
        dictDataMapper.insertDictData(dictData);
        return dictData.getDictDataId();
    }

    /**
     * 删除字典数据
     * @param dictDataId
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public void removeDictData(long dictDataId) {
        dictDataMapper.deleteDictData(dictDataId);
    }

    /**
     * 编辑字典数据
     * @param dictData
     * @author ren
     * @date 2025/05/18 13:49
     */
    @Override
    public void modifyDictData(DictData dictData, String updateBy) {
        dictData.setUpdateBy(updateBy);
        dictData.setUpdateTime(DateUtil.currentSeconds());
        dictDataMapper.updateDictData(dictData);
    }

    /**
     * 分页获取字典数据列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.DictData>
     * @author ren
     * @date 2025/05/18 13:50
     */
    @Override
    public IPage<DictData> listDictDataByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<DictData> dictDataList = dictDataMapper.listDictDataByPage(PageUtils.createPage(DictData.class),paramMap);
        return dictDataList;
    }

    /**
     * 获取字典数据详情
     * @param dictDataId
     * @return com.ren.system.entity.DictData
     * @author ren
     * @date 2025/05/18 13:50
     */
    @Override
    public DictData getDictDataById(long dictDataId) {
        DictData dictData = dictDataMapper.selectById(dictDataId);
        return dictData;
    }

    /**
     * 获取字典数据详情
     * @param dictType
     * @param isDefault
     * @return com.ren.system.entity.DictData
     * @author ren
     * @date 2025/05/23 14:05
     */
    @Override
    public DictData getDictDataByParam(String dictType , Byte isDefault) {
        DictData dictData = dictDataMapper.selectDictDataByParam(dictType,isDefault);
        return dictData;
    }

    /**
     * 获取字典数据列表
     * @param dictType
     * @return java.util.List<com.ren.system.entity.DictData>
     * @author ren
     * @date 2025/05/23 13:30
     */
    @Override
    public List<DictData> listDictDataByDictType(String dictType) {
        List<DictData> dictDataList = dictDataMapper.listDictDataByDictType(dictType);
        return dictDataList;
    }
}