package com.ren.monitor.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 缓存Key
 *
 * @author ren
 */
@Data
@AllArgsConstructor
public class CacheKeyVO
{
    /** 键 */
    private String key;
    /** 值 */
    private String value;

}