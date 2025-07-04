package com.ren.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.core.constant.HttpStatus;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.page.TableDataInfo;
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
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(int code, String message)
    {
        return AjaxResult.error(code,message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
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
