package com.orion.remote;

/**
 * 程序退出码
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/21 18:57
 */
public enum ExitCode {

    /**
     * 执行成功 0
     */
    SUCCESS(0),

    /**
     * 执行遇到错误 1
     */
    ERROR(1),

    /**
     * 程序没有 exitCode
     */
    NULL(null);

    private final Integer code;

    ExitCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
