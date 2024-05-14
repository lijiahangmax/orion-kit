package com.orion.net.host.ssh;

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

    /**
     * 判断是否执行成功
     *
     * @param exitCode exitCode
     * @return isSuccess
     */
    public static boolean isSuccess(Integer exitCode) {
        return SUCCESS.code.equals(exitCode);
    }

    /**
     * 判断是否执行失败
     *
     * @param exitCode exitCode
     * @return isFailed
     */
    public static boolean isFailed(Integer exitCode) {
        return !SUCCESS.code.equals(exitCode);
    }

}
