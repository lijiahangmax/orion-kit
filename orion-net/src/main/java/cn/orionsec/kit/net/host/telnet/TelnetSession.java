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

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.TerminalType;
import cn.orionsec.kit.net.host.telnet.command.TelnetCommandExecutor;
import cn.orionsec.kit.net.host.telnet.shell.TelnetShellExecutor;
import org.apache.commons.net.telnet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Telnet 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public class TelnetSession implements SafeCloseable {

    public static final int DEFAULT_TELNET_PORT = 23;

    private static final String DEFAULT_LOGIN_PROMPT = "login:";

    private static final String DEFAULT_PASSWORD_PROMPT = "Password:";

    private static final String DEFAULT_PROMPT = "$";

    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetSession.class);

    /**
     * telnet 客户端
     */
    private final TelnetClient client;

    /**
     * 标准输出流
     */
    private InputStream inputStream;

    /**
     * 标准输入流
     */
    private OutputStream outputStream;

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 连接超时时间 ms
     */
    private int timeout;

    /**
     * 阻塞读取超时时间 ms (0 为不超时)
     * <p>
     * 仅作用于登录等阻塞读取, 流式监听不受该超时限制
     */
    private int readTimeout;

    /**
     * 是否关闭 Nagle 算法
     */
    private boolean tcpNoDelay;

    /**
     * 是否开启 TCP 保活
     */
    private boolean keepAlive;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 编码
     */
    private String charset;

    /**
     * 登录提示符
     */
    private String loginPrompt;

    /**
     * 密码提示符
     */
    private String passwordPrompt;

    /**
     * 命令提示符
     */
    private String prompt;

    /**
     * 终端类型
     */
    private String terminalType;

    /**
     * 终端 行
     */
    private int cols;

    /**
     * 终端 列
     */
    private int rows;

    /**
     * 是否已连接
     */
    private volatile boolean connected;

    private TelnetSession(String host, int port) {
        Assert.notBlank(host, "host is blank");
        this.client = new TelnetClient();
        this.host = host;
        this.port = port;
        this.timeout = 0;
        this.readTimeout = 0;
        this.tcpNoDelay = true;
        this.keepAlive = true;
        this.charset = Const.UTF_8;
        this.loginPrompt = DEFAULT_LOGIN_PROMPT;
        this.passwordPrompt = DEFAULT_PASSWORD_PROMPT;
        this.prompt = DEFAULT_PROMPT;
        this.terminalType = TerminalType.XTERM.getType();
        this.cols = 180;
        this.rows = 36;
    }

    /**
     * @param host host
     * @return session
     */
    public static TelnetSession create(String host) {
        return create(host, DEFAULT_TELNET_PORT);
    }

    /**
     * @param host host
     * @param port port
     * @return session
     */
    public static TelnetSession create(String host, int port) {
        return new TelnetSession(host, port);
    }

    /**
     * @param host host
     * @return this
     */
    public TelnetSession host(String host) {
        this.host = host;
        return this;
    }

    /**
     * @param port port
     * @return this
     */
    public TelnetSession port(int port) {
        this.port = port;
        return this;
    }

    /**
     * @param timeout timeout
     * @return this
     */
    public TelnetSession timeout(int timeout) {
        Assert.gte(timeout, 0, "timeout must gte 0");
        this.timeout = timeout;
        return this;
    }

    /**
     * @param readTimeout readTimeout
     * @return this
     */
    public TelnetSession readTimeout(int readTimeout) {
        Assert.gte(readTimeout, 0, "readTimeout must gte 0");
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * @param username username
     * @return this
     */
    public TelnetSession username(String username) {
        this.username = username;
        return this;
    }

    /**
     * @param password password
     * @return this
     */
    public TelnetSession password(String password) {
        this.password = password;
        return this;
    }

    /**
     * @param charset charset
     * @return this
     */
    public TelnetSession charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * @param loginPrompt loginPrompt
     * @return this
     */
    public TelnetSession loginPrompt(String loginPrompt) {
        this.loginPrompt = loginPrompt;
        return this;
    }

    /**
     * @param passwordPrompt passwordPrompt
     * @return this
     */
    public TelnetSession passwordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
        return this;
    }

    /**
     * @param prompt prompt
     * @return this
     */
    public TelnetSession prompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * 设置终端类型
     *
     * @param type type
     * @return this
     */
    public TelnetSession terminalType(TerminalType type) {
        return this.terminalType(type.getType());
    }

    /**
     * 设置终端类型
     *
     * @param terminalType terminalType
     * @return this
     */
    public TelnetSession terminalType(String terminalType) {
        this.terminalType = terminalType;
        return this;
    }

    /**
     * 设置页面大小
     *
     * @param cols 行字数
     * @param rows 列数
     * @return this
     */
    public TelnetSession size(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        return this;
    }

    /**
     * 设置是否关闭 Nagle 算法
     * <p>
     * 交互式场景应保持开启 否则小包会被攒批 导致按键与回显延迟
     *
     * @param tcpNoDelay 是否关闭
     * @return this
     */
    public TelnetSession tcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    /**
     * 设置是否开启 TCP 保活
     *
     * @param keepAlive 是否开启
     * @return this
     */
    public TelnetSession keepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    /**
     * 告知服务端当前终端窗口大小
     *
     * @param cols 行字数
     * @param rows 列数
     */
    public void resize(int cols, int rows) {
        Assert.gt(cols, 0, "cols must gt 0");
        Assert.gt(rows, 0, "rows must gt 0");
        this.checkConnected();
        this.cols = cols;
        this.rows = rows;
        try {
            // IAC SB 31 <width16> <height16> IAC SE
            client.sendSubnegotiation(new int[]{
                    TelnetOption.WINDOW_SIZE,
                    (cols >> 8) & 0xFF, cols & 0xFF,
                    (rows >> 8) & 0xFF, rows & 0xFF});
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 建立连接
     *
     * @return this
     */
    public TelnetSession connect() {
        try {
            // 注册终端协商
            this.addOptionHandlers();
            // 设置连接超时
            if (timeout > 0) {
                client.setConnectTimeout(timeout);
                client.setDefaultTimeout(timeout);
            }
            // 建立连接
            client.connect(host, port);
            // 关闭 Nagle 算法 交互式场景下避免小包被攒批导致按键延迟
            client.setTcpNoDelay(tcpNoDelay);
            // 开启 TCP 保活 避免空闲连接被 NAT/防火墙静默断开
            client.setKeepAlive(keepAlive);
            // 获取输入输出流
            this.inputStream = client.getInputStream();
            this.outputStream = client.getOutputStream();
            this.connected = true;
            LOGGER.info("TelnetSession-connect connected {}:{}", host, port);
            // 登录
            login();
            return this;
        } catch (Exception e) {
            this.disconnect();
            throw Exceptions.connection(e);
        }
    }

    /**
     * 注册终端协商 option handlers
     *
     * @throws Exception Exception
     */
    private void addOptionHandlers() throws Exception {
        // 终端类型
        client.addOptionHandler(new TerminalTypeOptionHandler(terminalType, false, false, true, false));
        // 回显
        client.addOptionHandler(new EchoOptionHandler(false, false, false, true));
        // 抑制 go ahead
        client.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
        // 8bit 透明传输 保证中文等多字节编码不被破坏
        client.addOptionHandler(new SimpleOptionHandler(TelnetOption.BINARY, true, true, true, true));
        // 窗口大小
        client.addOptionHandler(new WindowSizeOptionHandler(cols, rows, true, false, true, false));
    }

    /**
     * 获取 shell 执行器
     *
     * @return executor
     */
    public TelnetShellExecutor getShellExecutor() {
        this.checkConnected();
        return new TelnetShellExecutor(client, inputStream, outputStream, prompt, charset, readTimeout);
    }

    /**
     * 获取命令执行器
     *
     * @param command command
     * @return executor
     */
    public TelnetCommandExecutor getCommandExecutor(String command) {
        this.checkConnected();
        return new TelnetCommandExecutor(client, inputStream, outputStream, prompt, charset, readTimeout, command);
    }

    /**
     * 检查是否已连接
     */
    private void checkConnected() {
        if (!connected) {
            throw Exceptions.connection("telnet session is not connected");
        }
    }

    /**
     * 断开连接
     * <p>
     * 幂等且不抛异常, 无论断开是否成功都会重置连接状态
     */
    public void disconnect() {
        try {
            if (client.isConnected()) {
                client.disconnect();
            }
        } catch (Exception e) {
            LOGGER.warn("TelnetSession-disconnect error", e);
        } finally {
            this.connected = false;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (Strings.isBlank(username)) {
            return;
        }
        LOGGER.info("TelnetSession-login start");
        try {
            if (Strings.isNotBlank(loginPrompt)) {
                // 读取登录提示
                readUntil(loginPrompt);
            }
            // 发送用户名
            writeLine(username);
            if (Strings.isNotBlank(password)) {
                if (Strings.isNotBlank(passwordPrompt)) {
                    // 读取密码提示
                    readUntil(passwordPrompt);
                }
                // 发送密码
                writeLine(password);
            }
            if (Strings.isNotBlank(prompt)) {
                // 读取命令提示符
                readUntil(prompt);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        LOGGER.info("TelnetSession-login done");
    }

    /**
     * @param command command
     * @throws IOException IOException
     */
    private void writeLine(String command) throws IOException {
        outputStream.write(Strings.bytes(command + Const.LF, charset));
        outputStream.flush();
    }

    /**
     * 阻塞读取直到命中指定内容
     *
     * @param pattern pattern
     * @return result
     * @throws IOException IOException
     */
    private String readUntil(String pattern) throws IOException {
        client.setSoTimeout(readTimeout);
        try {
            return TelnetReads.readUntil(inputStream, pattern, charset, readTimeout, Const.BUFFER_KB_32);
        } finally {
            this.resetSoTimeout();
        }
    }

    /**
     * 关闭 socket 读超时
     */
    private void resetSoTimeout() {
        try {
            client.setSoTimeout(0);
        } catch (IOException e) {
            // 连接已关闭时忽略
        }
    }

    /**
     * @return 是否已连接
     */
    public boolean isConnected() {
        return connected && client.isConnected();
    }

    /**
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @return loginPrompt
     */
    public String getLoginPrompt() {
        return loginPrompt;
    }

    /**
     * @return passwordPrompt
     */
    public String getPasswordPrompt() {
        return passwordPrompt;
    }

    /**
     * @return prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @return 终端类型
     */
    public String getTerminalType() {
        return terminalType;
    }

    /**
     * @return 行字数
     */
    public int getCols() {
        return cols;
    }

    /**
     * @return 列数
     */
    public int getRows() {
        return rows;
    }

    /**
     * 关闭会话
     */
    @Override
    public void close() {
        Streams.close(inputStream);
        Streams.close(outputStream);
        this.disconnect();
    }

}
