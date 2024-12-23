/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.net.host.ssh;

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
