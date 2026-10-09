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
package cn.orionsec.kit.net.telnet;

/**
 * Telnet 登录失败原因
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/2
 */
public enum TelnetLoginFailReason {

    /**
     * 对端关闭连接
     */
    EOF,

    /**
     * 设备重新返回登录提示符
     */
    LOGIN_PROMPT_RETURNED,

    /**
     * 设备重新返回密码提示符
     */
    PASSWORD_PROMPT_RETURNED,

    /**
     * 设备明确输出认证失败信息
     */
    LOGIN_REJECTED,

    /**
     * 无法确认登录完成
     */
    PROMPT_UNCONFIRMED,

    /**
     * 登录阶段读取超时
     */
    READ_TIMEOUT,

    /**
     * 登录阶段读取失败
     */
    READ_FAILED

}
