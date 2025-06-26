package com.ren.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.domain.constant.HttpStatus;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * web层通用数据处理
 * 
 * @author ren
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
     * 获取分页返回对象
     * @param list
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/05/13 16:33
     */
    protected TableDataInfo getDataTable(IPage<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list.getRecords());
        rspData.setTotal(list.getTotal());
        rspData.setTotalPage(list.getPages());
        rspData.setPageNum(list.getCurrent());
        rspData.setPageSize(list.getSize());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResultDTO success()
    {
        return AjaxResultDTO.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResultDTO error()
    {
        return AjaxResultDTO.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResultDTO success(String message)
    {
        return AjaxResultDTO.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResultDTO success(Object data)
    {
        return AjaxResultDTO.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResultDTO error(String message)
    {
        return AjaxResultDTO.error(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResultDTO error(int code, String message)
    {
        return AjaxResultDTO.error(code,message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResultDTO warn(String message)
    {
        return AjaxResultDTO.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResultDTO toAjax(int rows)
    {
        return rows > 0 ? AjaxResultDTO.success() : AjaxResultDTO.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResultDTO toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

}
