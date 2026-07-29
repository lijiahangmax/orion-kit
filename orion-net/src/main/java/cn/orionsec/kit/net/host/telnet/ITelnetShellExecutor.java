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
package cn.orionsec.kit.net.host.telnet;

import cn.orionsec.kit.net.host.ssh.TerminalType;

/**
 * Telnet shell 执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public interface ITelnetShellExecutor extends ITelnetExecutor {

    /**
     * 设置终端类型
     * <p>
     * 终端协商在 {@link TelnetSession#connect()} 建连时完成
     * 建连后设置仅更新查询值 不会重新协商
     *
     * @param type type
     */
    default void terminalType(TerminalType type) {
        this.terminalType(type.getType());
    }

    /**
     * 设置终端类型
     * <p>
     * 终端协商在 {@link TelnetSession#connect()} 建连时完成
     * 建连后设置仅更新查询值 不会重新协商
     *
     * @param terminalType terminalType
     */
    void terminalType(String terminalType);

    /**
     * 设置页面大小
     * <p>
     * 窗口大小协商在 {@link TelnetSession#connect()} 建连时完成
     * 建连后设置仅更新查询值 不会重新协商
     *
     * @param cols 行字数
     * @param rows 列数
     */
    void size(int cols, int rows);

    /**
     * 获取设置的终端类型
     *
     * @return 终端类型
     */
    String getTerminalType();

    /**
     * 获取 行字数
     *
     * @return 行字数
     */
    int getCols();

    /**
     * 获取 列数
     *
     * @return 列数
     */
    int getRows();

}
