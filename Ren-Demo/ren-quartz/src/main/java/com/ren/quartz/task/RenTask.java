package com.ren.quartz.task;

import org.springframework.stereotype.Component;

import com.ren.common.utils.StringUtils;

/**
 * RenTask 默认定时任务
 *
 * @author ren
 * @version 2025/06/26 20:08
 **/
@Component("renTask")
public class RenTask {

    /**
     * 默认多参定时器任务
     * 
     * @author ren
     * @date 2025/06/26 21:15
     */
    public void renMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    /**
     * 默认有参定时器任务
     * 
     * @author ren
     * @date 2025/06/26 21:15
     */
    public void renParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    /**
     * 默认无参定时器任务
     * 
     * @author ren
     * @date 2025/06/26 21:15
     */
    public void renNoParams() {
        System.out.println("执行无参方法");
    }

}