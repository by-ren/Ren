package com.ren.common.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.common.domain.page.PageDomain;

/**
 * 表格数据处理
 * 
 * @author ren
 */
public class PageUtils
{
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序字段
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序方式 "desc" 或者 "asc".
     */
    public static final String ORDER_BY_WAY = "orderByWay";

    /**
     * 封装分页对象
     */
    public static void setThreadLocalPage()
    {
        PageDomain.PAGE_NUM.set(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        PageDomain.PAGE_SIZE.set(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        String orderByColumn = Convert.toStr(ServletUtils.getParameter(ORDER_BY_COLUMN),"");
        if(StringUtils.isNotBlank(orderByColumn)){
            //将小驼峰转为下划线
            orderByColumn = StringUtils.toUnderlineCase(orderByColumn);
            /*if(StringUtils.contains(orderByColumn,"_str")){
                //截取第一个_str之前的部分，false表示不包含_str（匹配日期类型的字段）
                orderByColumn = StringUtils.subBefore(orderByColumn, "_str", false);
            }*/
        }
        PageDomain.ORDER_BY_COLUMN.set(orderByColumn);
        PageDomain.ORDER_BY_WAY.set(Convert.toStr(ServletUtils.getParameter(ORDER_BY_WAY),""));
    }

    /**
     * 清除分页对象
     */
    public static void cleanThreadLocalPage()
    {
        PageDomain.clear();
    }


    public static <T> Page<T> createPage(Class<T> clazz){

        Page<T> page = new Page<>(PageDomain.PAGE_NUM.get(),PageDomain.PAGE_SIZE.get());
        if(StringUtils.isNotBlank(PageDomain.ORDER_BY_COLUMN.get()) && StringUtils.isNotBlank(PageDomain.ORDER_BY_WAY.get())){
            OrderItem orderItem = new OrderItem(PageDomain.ORDER_BY_COLUMN.get(),PageDomain.ORDER_BY_WAY.get().equalsIgnoreCase("asc"));
            page.addOrder(orderItem);
        }
        return page;

    }

}
