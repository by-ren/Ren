package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.utils.PageUtils;
import com.ren.system.entity.DictType;
import com.ren.system.mapper.DictTypeMapper;
import com.ren.system.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeMapper;

    /*
     * 添加字典类型
     * @param dictType
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public long addDictType(DictType dictType,String createBy) {
        dictType.setCreateBy(createBy);
        dictType.setCreateTime(DateUtil.currentSeconds());
        dictTypeMapper.insertDictType(dictType);
        return dictType.getDictTypeId();
    }

    /*
     * 删除字典类型
     * @param dictTypeId
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public void removeDictType(long dictTypeId) {
        dictTypeMapper.deleteDictType(dictTypeId);
    }

    /*
     * 编辑字典类型
     * @param dictType
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public void modifyDictType(DictType dictType) {
        dictTypeMapper.updateDictType(dictType);
    }

    /*
     * 分页获取字典类型列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.DictType>
     * @author admin
     * @date 2025/05/18 13:50
     */
    @Override
    public IPage<DictType> listDictTypeByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<DictType> dictTypeList = dictTypeMapper.listDictTypeByPage(PageUtils.createPage(DictType.class),paramMap);
        return dictTypeList;
    }

    /*
     * 获取字典类型详情
     * @param dictTypeId
     * @return com.ren.system.entity.DictType
     * @author admin
     * @date 2025/05/18 13:50
     */
    @Override
    public DictType getDictTypeById(long dictTypeId) {
        DictType dictType = dictTypeMapper.selectById(dictTypeId);
        return dictType;
    }
}