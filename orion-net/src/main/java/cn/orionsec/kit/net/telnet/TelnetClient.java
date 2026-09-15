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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.exception.ConnectionRuntimeException;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.TerminalType;
import cn.orionsec.kit.net.telnet.command.TelnetCommandExecutor;
import cn.orionsec.kit.net.telnet.shell.TelnetShellExecutor;
import org.apache.commons.net.telnet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Telnet 客户端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public class TelnetClient implements ITelnetClient {

    private static final String DEFAULT_LOGIN_PROMPT = "login:";

    private static final String DEFAULT_PASSWORD_PROMPT = "Password:";

    private static final String DEFAULT_PROMPT = "$";

    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetClient.class);

    private final org.apache.commons.net.telnet.TelnetClient client;

    private InputStream inputStream;

    private OutputStream outputStream;

    private final String host;

    private int port;

    private String username;

    private String password;

    private int readTimeout;

    private boolean tcpNoDelay;

    private String charset;

    private String loginPrompt;

    private String passwordPrompt;

    private String prompt;

    private String terminalType;

    private int cols;

    private int rows;

    private volatile boolean connected;

    private boolean initialed;

    private TelnetClient(String host, int port) {
        Assert.notBlank(host, "host is blank");
        this.client = new org.apache.commons.net.telnet.TelnetClient();
        this.host = host;
        this.port = port;
        this.readTimeout = 0;
        this.tcpNoDelay = true;
        this.charset = Const.UTF_8;
        this.loginPrompt = DEFAULT_LOGIN_PROMPT;
        this.passwordPrompt = DEFAULT_PASSWORD_PROMPT;
        this.prompt = DEFAULT_PROMPT;
        this.terminalType = TerminalType.XTERM.getType();
        this.cols = 180;
        this.rows = 36;
    }

    public static TelnetClient create(String host) {
        return create(host, DEFAULT_TELNET_PORT);
    }

    /**
     * 创建会话
     *
     * @param host host
     * @param port port
     * @return session
     */
    public static TelnetClient create(String host, int port) {
        return new TelnetClient(host, port);
    }

    /**
     * 设置端口
     *
     * @param port port
     * @return this
     */
    public TelnetClient port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout timeout
     * @return this
     */
    public TelnetClient connectTimeout(int timeout) {
        Assert.gte(timeout, 0, "the time must greater than or equal 0");
        client.setConnectTimeout(timeout);
        return this;
    }

    /**
     * 设置阻塞读取超时时间
     *
     * @param readTimeout timeout
     * @return this
     */
    public TelnetClient readTimeout(int readTimeout) {
        Assert.gte(readTimeout, 0, "readTimeout must gte 0");
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * 设置用户名
     *
     * @param username username
     * @return this
     */
    public TelnetClient username(String username) {
        this.username = username;
        return this;
    }

    /**
     * 设置密码
     *
     * @param password password
     * @return this
     */
    public TelnetClient password(String password) {
        this.password = password;
        return this;
    }

    /**
     * 设置字符编码
     *
     * @param charset charset
     * @return this
     */
    public TelnetClient charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 设置登录提示符
     *
     * @param loginPrompt loginPrompt
     * @return this
     */
    public TelnetClient loginPrompt(String loginPrompt) {
        this.loginPrompt = loginPrompt;
        return this;
    }

    /**
     * 设置密码提示符
     *
     * @param passwordPrompt passwordPrompt
     * @return this
     */
    public TelnetClient passwordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
        return this;
    }

    /**
     * 设置命令提示符
     *
     * @param prompt prompt
     * @return this
     */
    public TelnetClient prompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * 设置终端类型
     *
     * @param type type
     * @return this
     */
    public TelnetClient terminalType(TerminalType type) {
        return this.terminalType(type.getType());
    }

    /**
     * 设置终端类型
     *
     * @param terminalType terminalType
     * @return this
     */
    public TelnetClient terminalType(String terminalType) {
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
    public TelnetClient size(int cols, int rows) {
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
    public TelnetClient tcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    /**
     * 发送保活信号
     *
     * @return this
     */
    public TelnetClient keepAlive() {
        this.checkConnected();
        try {
            client.sendCommand((byte) TelnetCommand.NOP);
        } catch (Exception e) {
            // ignored
        }
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
        } catch (Exception e) {
            // ignored
        }
    }

    /**
     * 建立连接
     *
     * @return this
     */
    public TelnetClient connect() {
        try {
            // 添加参数
            this.addOptionHandlers();
            // 建立连接
            client.connect(this.host, this.port);
            client.setTcpNoDelay(this.tcpNoDelay);
            this.inputStream = client.getInputStream();
            this.outputStream = client.getOutputStream();
            this.connected = true;
            LOGGER.info("TelnetClient-connect connected {}:{}", this.host, this.port);
        } catch (Exception e) {
            this.disconnect();
            throw Exceptions.connection(e);
        }

        try {
            // 登录
            this.login(username, password);
        } catch (Exception e) {
            // 登录失败一般是账号密码错误或提示符不匹配
            this.disconnect();
            if (e instanceof ConnectionRuntimeException) {
                throw (ConnectionRuntimeException) e;
            }
            throw Exceptions.authentication("telnet login fail", e);
        }
        return this;
    }

    /**
     * 注册终端协商 option handler
     *
     * @throws Exception Exception
     */
    private void addOptionHandlers() throws Exception {
        if (this.initialed) {
            return;
        }
        // 终端类型
        client.addOptionHandler(new TerminalTypeOptionHandler(this.terminalType, false, false, true, false));
        // 回显
        client.addOptionHandler(new EchoOptionHandler(false, false, false, true));
        // 抑制 go ahead
        client.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
        // 8bit 透明传输 保证中文等多字节编码不被破坏
        client.addOptionHandler(new SimpleOptionHandler(TelnetOption.BINARY, true, true, true, true));
        // 窗口大小
        client.addOptionHandler(new WindowSizeOptionHandler(this.cols, this.rows, true, false, true, false));
        this.initialed = true;
    }

    /**
     * 获取 shell 执行器
     *
     * @return executor
     */
    public TelnetShellExecutor getShellExecutor() {
        // 检查是否已连接
        this.checkConnected();
        return new TelnetShellExecutor(client, this.inputStream, this.outputStream, this.prompt, this.charset, this.readTimeout);
    }

    /**
     * 获取命令执行器
     *
     * @param command command
     * @return executor
     */
    public TelnetCommandExecutor getCommandExecutor(String command) {
        // 检查是否已连接
        this.checkConnected();
        return new TelnetCommandExecutor(client, this.inputStream, this.outputStream, this.prompt, this.charset, this.readTimeout, command);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @throws IOException IOException
     */
    private void login(String username, String password) throws IOException {
        if (Strings.isBlank(username)) {
            return;
        }
        LOGGER.info("TelnetClient-login start");
        // 读取登录提示
        if (Strings.isNotBlank(this.loginPrompt)) {
            this.readUntil(this.loginPrompt);
        }
        // 发送用户名
        this.writeLine(username);

        if (Strings.isNotBlank(password)) {
            // 读取密码提示
            if (Strings.isNotBlank(this.passwordPrompt)) {
                this.readUntil(this.passwordPrompt);
            }
            // 发送密码
            this.writeLine(password);
        }

        // 读取命令提示符
        if (Strings.isNotBlank(this.prompt)) {
            this.readUntil(this.prompt);
        }
        LOGGER.info("TelnetClient-login done");
    }

    /**
     * 写入行
     *
     * @param command command
     * @throws IOException IOException
     */
    private void writeLine(String command) throws IOException {
        this.outputStream.write(Strings.bytes(command + Const.LF, this.charset));
        this.outputStream.flush();
    }

    /**
     * 阻塞读取直到命中指定内容
     *
     * @param pattern pattern
     * @return result
     * @throws IOException IOException
     */
    private String readUntil(String pattern) throws IOException {
        this.checkConnected();
        client.setSoTimeout(this.readTimeout);
        try {
            return TelnetReads.readUntil(this.inputStream, pattern, this.charset, this.readTimeout, Const.BUFFER_KB_32);
        } finally {
            this.resetSoTimeout();
        }
    }

    /**
     * 关闭 socket 读超时
     */
    private void resetSoTimeout() {
        if (!client.isConnected()) {
            return;
        }
        try {
            client.setSoTimeout(0);
        } catch (Exception e) {
            // ignored
        }
    }

    /**
     * 检查是否已连接
     */
    private void checkConnected() {
        if (!this.isConnected()) {
            throw Exceptions.connection("telnet session is not connected");
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            if (client.isConnected()) {
                client.disconnect();
            }
        } catch (Exception e) {
            LOGGER.warn("TelnetClient-disconnect error", e);
        } finally {
            this.connected = false;
        }
    }

    /**
     * @return 是否已连接
     */
    public boolean isConnected() {
        return connected && client.isConnected();
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getUsername() {
        return username;
    }

    public String getCharset() {
        return this.charset;
    }

    public String getLoginPrompt() {
        return this.loginPrompt;
    }

    public String getPasswordPrompt() {
        return this.passwordPrompt;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getTerminalType() {
        return this.terminalType;
    }

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }

    @Override
    public void close() {
        Streams.close(this.inputStream);
        Streams.close(this.outputStream);
        this.disconnect();
    }

}
