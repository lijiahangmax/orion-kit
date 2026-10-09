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
import cn.orionsec.kit.lang.exception.AuthenticationException;
import cn.orionsec.kit.lang.exception.ConnectionRuntimeException;
import cn.orionsec.kit.lang.exception.OutputException;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
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
import java.util.Collection;
import java.util.List;

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

    private static final String LOGIN_FAIL_MESSAGE = "telnet login fail";

    private static final int DEFAULT_PROMPT_IDLE_MS = 300;

    private static final int DEFAULT_PROMPT_WAIT_MS = 10000;

    private static final int DEFAULT_LOGIN_TIMEOUT = 30000;

    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetClient.class);

    private final org.apache.commons.net.telnet.TelnetClient client;

    private InputStream inputStream;

    private OutputStream outputStream;

    private final String host;

    private int port;

    private String username;

    private String password;

    private int loginTimeout;

    /**
     * 登录后探测到的命令提示符
     */
    private String detectedPrompt;

    /**
     * 登录阶段输出
     */
    private final StringBuilder loginOutput;

    /**
     * 提示符探测静默阈值 ms
     */
    private int promptIdleMs;

    /**
     * 提示符探测最大等待 ms
     */
    private int promptWaitMs;

    private boolean tcpNoDelay;

    private String charset;

    /**
     * 登录提示符候选 (空集合为跳过等待)
     */
    private List<String> loginPrompts;

    /**
     * 密码提示符候选 (空集合为跳过等待)
     */
    private List<String> passwordPrompts;

    /**
     * 命令提示符候选 (空集合为跳过登录完成检测)
     */
    private List<String> prompts;

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
        this.cols = 180;
        this.rows = 36;
        this.charset = Const.UTF_8;
        this.tcpNoDelay = true;
        this.loginTimeout = DEFAULT_LOGIN_TIMEOUT;
        this.loginOutput = new StringBuilder();
        this.promptIdleMs = DEFAULT_PROMPT_IDLE_MS;
        this.promptWaitMs = DEFAULT_PROMPT_WAIT_MS;
        this.terminalType = TerminalType.XTERM.getType();
        this.loginPrompts = Lists.singleton(DEFAULT_LOGIN_PROMPT);
        this.passwordPrompts = Lists.singleton(DEFAULT_PASSWORD_PROMPT);
        this.prompts = Lists.empty();
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
     * 设置登录阶段读取超时时间 (默认 30s, 0 为不超时)
     *
     * @param loginTimeout loginTimeout
     * @return this
     */
    public TelnetClient loginTimeout(int loginTimeout) {
        Assert.gte(loginTimeout, 0, "loginTimeout must gte 0");
        this.loginTimeout = loginTimeout;
        return this;
    }

    /**
     * 设置登录后读取/判定的超时参数
     *
     * @param idleMs    静默阈值 ms
     * @param maxWaitMs 最大等待 ms
     * @return this
     */
    public TelnetClient promptDetect(int idleMs, int maxWaitMs) {
        Assert.gt(idleMs, 0, "idleMs must gt 0");
        Assert.gte(maxWaitMs, 0, "maxWaitMs must gte 0");
        this.promptIdleMs = idleMs;
        this.promptWaitMs = maxWaitMs > 0 ? maxWaitMs : DEFAULT_PROMPT_WAIT_MS;
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
        this.loginPrompts = TelnetLoginJudge.normalize(Lists.singleton(loginPrompt));
        return this;
    }

    /**
     * 设置登录提示符候选
     *
     * @param loginPrompts loginPrompts
     * @return this
     */
    public TelnetClient loginPrompt(Collection<String> loginPrompts) {
        this.loginPrompts = TelnetLoginJudge.normalize(loginPrompts);
        return this;
    }

    /**
     * 设置密码提示符
     *
     * @param passwordPrompt passwordPrompt
     * @return this
     */
    public TelnetClient passwordPrompt(String passwordPrompt) {
        this.passwordPrompts = TelnetLoginJudge.normalize(Lists.singleton(passwordPrompt));
        return this;
    }

    /**
     * 设置密码提示符候选
     *
     * @param passwordPrompts passwordPrompts
     * @return this
     */
    public TelnetClient passwordPrompt(Collection<String> passwordPrompts) {
        this.passwordPrompts = TelnetLoginJudge.normalize(passwordPrompts);
        return this;
    }

    /**
     * 设置命令提示符
     *
     * @param prompt prompt
     * @return this
     */
    public TelnetClient prompt(String prompt) {
        this.prompts = TelnetLoginJudge.normalize(Lists.singleton(prompt));
        return this;
    }

    /**
     * 设置命令提示符候选 (空集合跳过登录完成检测)
     *
     * @param prompts prompts
     * @return this
     */
    public TelnetClient prompt(Collection<String> prompts) {
        this.prompts = TelnetLoginJudge.normalize(prompts);
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
            switch (e) {
                case OutputException ex -> throw ex;
                case AuthenticationException ex -> throw ex;
                case ConnectionRuntimeException ex -> throw ex;
                default -> throw Exceptions.authentication(LOGIN_FAIL_MESSAGE, e);
            }
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
        return new TelnetShellExecutor(client, this.inputStream, this.outputStream, this.prompts, this.charset, this.loginTimeout);
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
        return new TelnetCommandExecutor(client, this.inputStream, this.outputStream, this.prompts, this.charset, this.loginTimeout, command);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    private void login(String username, String password) {
        if (Strings.isBlank(username)) {
            return;
        }
        LOGGER.info("TelnetClient-login start");
        try {
            // 读取登录提示
            if (!this.loginPrompts.isEmpty()) {
                TelnetReadResult result = this.readUntilCandidates(this.loginPrompts);
                this.appendLoginOutput(result.getContent());
                this.checkLoginReadResult(result);
            }
            // 发送用户名
            this.writeLine(username);

            // 读取密码提示
            if (!Strings.isBlank(password)) {
                if (!this.passwordPrompts.isEmpty()) {
                    TelnetReadResult result = this.readUntilCandidates(this.passwordPrompts);
                    this.appendLoginOutput(result.getContent());
                    this.checkLoginReadResult(result);
                }
                // 发送密码
                this.writeLine(password);
            }

            // 登录后读取信息页
            TelnetReadResult pageResult = this.readLoginPage();
            this.appendLoginPage(pageResult.getContent());
            if (pageResult.isEof()) {
                throw this.loginFailure(TelnetLoginFailReason.EOF, null);
            }
            if (pageResult.isTimeout()) {
                throw this.loginFailure(TelnetLoginFailReason.READ_TIMEOUT, null);
            }
            // 判定登录结果
            TelnetLoginResult result = TelnetLoginJudge.judge(pageResult.getContent(), this.loginPrompts, this.passwordPrompts, this.prompts, false);
            if (result.isFailure()) {
                throw this.loginFailure(result.getReason(), null);
            }
            // 探测真实命令提示符
            this.detectedPrompt = result.getDetectedPrompt();
            if (this.detectedPrompt != null) {
                LOGGER.info("TelnetClient-login detected prompt: {}", this.detectedPrompt);
            }
            LOGGER.info("TelnetClient-login done");
        } catch (OutputException e) {
            throw e;
        } catch (Exception e) {
            throw this.loginFailure(TelnetLoginFailReason.READ_FAILED, e);
        }
    }

    /**
     * 构建登录失败异常并记录失败原因
     *
     * @param reason 失败原因
     * @param cause  底层异常
     * @return 登录失败异常
     */
    private OutputException loginFailure(TelnetLoginFailReason reason, Throwable cause) {
        if (cause == null) {
            LOGGER.warn("TelnetClient-login fail reason: {}", reason);
        } else {
            LOGGER.warn("TelnetClient-login fail reason: {}", reason, cause);
        }
        // 携带设备原始输出 并嵌套认证异常 供上层识别登录失败与回显
        return Exceptions.output(this.getLoginOutput(), LOGIN_FAIL_MESSAGE, Exceptions.authentication(LOGIN_FAIL_MESSAGE, cause));
    }

    /**
     * 检查登录提示读取结果
     *
     * @param result 读取结果
     */
    private void checkLoginReadResult(TelnetReadResult result) {
        if (result.isEof()) {
            throw this.loginFailure(TelnetLoginFailReason.EOF, null);
        }
        if (result.isTimeout()) {
            throw this.loginFailure(TelnetLoginFailReason.READ_TIMEOUT, null);
        }
    }

    /**
     * 读取登录后输出直到判定可结算或达到最大等待
     *
     * @return 读取结果
     * @throws IOException IOException
     */
    private TelnetReadResult readLoginPage() throws IOException {
        this.checkConnected();
        try {
            client.setSoTimeout(this.promptIdleMs);
            return TelnetReads.readUntilDecided(this.inputStream, this.charset, this.promptWaitMs, Const.BUFFER_KB_32,
                    // 数据到达结算
                    page -> !TelnetLoginJudge.judgeReading(page, this.loginPrompts, this.passwordPrompts, this.prompts).isUndecided(),
                    // 静默结算
                    page -> !TelnetLoginJudge.judgeSettled(page, this.loginPrompts, this.passwordPrompts, this.prompts).isUndecided());
        } finally {
            this.resetSoTimeout();
        }
    }

    /**
     * 读取直到命中任一候选
     *
     * @param patterns 候选列表
     * @return 读取结果
     * @throws IOException IOException
     */
    private TelnetReadResult readUntilCandidates(Collection<String> patterns) throws IOException {
        // 检查是否已连接
        this.checkConnected();
        client.setSoTimeout(this.loginTimeout);
        try {
            return TelnetReads.readUntilResult(this.inputStream, patterns, this.charset, this.loginTimeout, Const.BUFFER_KB_32);
        } finally {
            this.resetSoTimeout();
        }
    }

    /**
     * 追加登录输出 (登录/密码提示阶段)
     *
     * @param output output
     */
    private void appendLoginOutput(String output) {
        if (!Strings.isBlank(output)) {
            loginOutput.append(output);
        }
    }

    /**
     * 追加 shell 信息页到登录输出缓冲 (丢弃设备回显的密码行)
     *
     * @param page 信息页内容
     */
    private void appendLoginPage(String page) {
        if (Strings.isBlank(page)) {
            return;
        }
        int index = page.indexOf('\n');
        String firstLine = (index == -1 ? page : page.substring(0, index)).trim();
        if (Strings.isNotBlank(this.password) && firstLine.equals(this.password)) {
            page = index == -1 ? Const.EMPTY : page.substring(index + 1);
        }
        if (!Strings.isBlank(page)) {
            loginOutput.append(page);
        }
    }

    /**
     * 写入行
     *
     * @param command command
     * @throws IOException IOException
     */
    private void writeLine(String command) throws IOException {
        outputStream.write(Strings.bytes(command + Const.LF, this.charset));
        outputStream.flush();
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

    public String getDetectedPrompt() {
        return this.detectedPrompt;
    }

    public String getLoginOutput() {
        return this.loginOutput.isEmpty() ? null : this.loginOutput.toString();
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
