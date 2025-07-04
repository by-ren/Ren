package com.ren.quartz.core.exception;

/**
 * 定时任务异常类
 * 
 * @author ren
 * @date 2025/06/23 15:53
 */
public class QuartzException extends Exception {
    private static final long serialVersionUID = 1L;

    private final Code code;

    public QuartzException(String msg, Code code) {
        this(msg, code, null);
    }

    public QuartzException(String msg, Code code, Exception nestedEx) {
        super(msg, nestedEx);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public enum Code {
        TASK_EXISTS, NO_TASK_EXISTS, TASK_ALREADY_STARTED, UNKNOWN, CONFIG_ERROR, TASK_NODE_NOT_AVAILABLE
    }
}