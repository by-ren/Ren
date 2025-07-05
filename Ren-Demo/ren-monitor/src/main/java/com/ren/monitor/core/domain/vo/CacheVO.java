package com.ren.monitor.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 缓存
 *
 * @author ren
 */
@Data
@AllArgsConstructor
public class CacheVO
{
    /** 缓存名称 */
    private String name;
    /** 缓存备注 */
    private String remark;

}