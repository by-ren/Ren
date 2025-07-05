package com.ren.common.core.page;


/**
 * 分页数据
 * 
 * @author ren
 */
public class PageDomain
{

    /** 当前记录起始索引 */
    public static final ThreadLocal<Integer> PAGE_NUM = new ThreadLocal<>();

    /** 每页显示记录数 */
    public static final ThreadLocal<Integer> PAGE_SIZE = new ThreadLocal<>();

    /** 排序字段 */
    public static final ThreadLocal<String> ORDER_BY_COLUMN = new ThreadLocal<>();

    /** 排序方式 */
    public static final ThreadLocal<String> ORDER_BY_WAY = new ThreadLocal<>();

    public static void clear() {
        PAGE_NUM.remove();
        PAGE_SIZE.remove();
        ORDER_BY_COLUMN.remove();
        ORDER_BY_WAY.remove();
    }

}