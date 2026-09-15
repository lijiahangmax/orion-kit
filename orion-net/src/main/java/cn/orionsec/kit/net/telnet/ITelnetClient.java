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

import cn.orionsec.kit.lang.able.Connectable;
import cn.orionsec.kit.net.TerminalType;
import cn.orionsec.kit.net.telnet.command.TelnetCommandExecutor;
import cn.orionsec.kit.net.telnet.shell.TelnetShellExecutor;

/**
 * Telnet 客户端 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public interface ITelnetClient extends Connectable {

    int DEFAULT_TELNET_PORT = 23;

    /**
     * 设置端口
     *
     * @param port port
     * @return this
     */
    ITelnetClient port(int port);

    /**
     * 设置连接超时时间
     *
     * @param timeout timeout
     * @return this
     */
    ITelnetClient connectTimeout(int timeout);

    /**
     * 设置阻塞读取超时时间
     *
     * @param readTimeout timeout
     * @return this
     */
    ITelnetClient readTimeout(int readTimeout);

    /**
     * 设置用户名
     *
     * @param username username
     * @return this
     */
    ITelnetClient username(String username);

    /**
     * 设置密码
     *
     * @param password password
     * @return this
     */
    ITelnetClient password(String password);

    /**
     * 设置字符编码
     *
     * @param charset charset
     * @return this
     */
    ITelnetClient charset(String charset);

    /**
     * 设置登录提示符
     *
     * @param loginPrompt loginPrompt
     * @return this
     */
    ITelnetClient loginPrompt(String loginPrompt);

    /**
     * 设置密码提示符
     *
     * @param passwordPrompt passwordPrompt
     * @return this
     */
    ITelnetClient passwordPrompt(String passwordPrompt);

    /**
     * 设置命令提示符
     *
     * @param prompt prompt
     * @return this
     */
    ITelnetClient prompt(String prompt);

    /**
     * 设置终端类型
     *
     * @param type type
     * @return this
     */
    ITelnetClient terminalType(TerminalType type);

    /**
     * 设置终端类型
     *
     * @param terminalType terminalType
     * @return this
     */
    ITelnetClient terminalType(String terminalType);

    /**
     * 设置页面大小
     *
     * @param cols 行字数
     * @param rows 列数
     * @return this
     */
    ITelnetClient size(int cols, int rows);

    /**
     * 设置是否关闭 Nagle 算法
     * <p>
     * 交互式场景应保持开启 否则小包会被攒批 导致按键与回显延迟
     *
     * @param tcpNoDelay 是否关闭
     * @return this
     */
    ITelnetClient tcpNoDelay(boolean tcpNoDelay);

    /**
     * 发送保活信号
     *
     * @return this
     */
    ITelnetClient keepAlive();

    /**
     * 告知服务端当前终端窗口大小
     *
     * @param cols 行字数
     * @param rows 列数
     */
    void resize(int cols, int rows);

    /**
     * 建立连接
     *
     * @return this
     */
    ITelnetClient connect();

    /**
     * 获取 shell 执行器
     *
     * @return executor
     */
    TelnetShellExecutor getShellExecutor();

    /**
     * 获取命令执行器
     *
     * @param command command
     * @return executor
     */
    TelnetCommandExecutor getCommandExecutor(String command);

    /**
     * @return 主机
     */
    String getHost();

    /**
     * @return 端口
     */
    int getPort();

    /**
     * @return 用户名
     */
    String getUsername();

    /**
     * @return 编码
     */
    String getCharset();

    /**
     * @return 登录提示符
     */
    String getLoginPrompt();

    /**
     * @return 密码提示符
     */
    String getPasswordPrompt();

    /**
     * @return 命令提示符
     */
    String getPrompt();

    /**
     * @return 终端类型
     */
    String getTerminalType();

    /**
     * @return 行字数
     */
    int getCols();

    /**
     * @return 列数
     */
    int getRows();

}
