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
import org.apache.commons.net.telnet.TelnetClient;
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

    private final TelnetClient client;

    private InputStream inputStream;

    private OutputStream outputStream;

    private String host;

    private int port;

    private int timeout;

    private int readTimeout;

    private String username;

    private String password;

    private String charset;

    private String loginPrompt;

    private String passwordPrompt;

    private String prompt;

    private volatile boolean connected;

    private TelnetSession(String host, int port) {
        Assert.notBlank(host, "host is blank");
        this.client = new TelnetClient();
        this.host = host;
        this.port = port;
        this.timeout = 0;
        this.readTimeout = 0;
        this.charset = Const.UTF_8;
        this.loginPrompt = DEFAULT_LOGIN_PROMPT;
        this.passwordPrompt = DEFAULT_PASSWORD_PROMPT;
        this.prompt = DEFAULT_PROMPT;
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
     * 建立连接
     *
     * @return this
     */
    public TelnetSession connect() {
        try {
            // 设置超时
            if (timeout > 0) {
                client.setConnectTimeout(timeout);
                client.setDefaultTimeout(timeout);
            }
            // 建立连接
            client.connect(host, port);
            // 获取输入输出流
            inputStream = client.getInputStream();
            outputStream = client.getOutputStream();
            connected = true;
            LOGGER.info("TelnetSession-connect connected {}:{}", host, port);
            // 登录
            login();
            return this;
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 获取执行器
     *
     * @return executor
     */
    public TelnetExecutor getExecutor() {
        if (!connected) {
            throw Exceptions.connection("telnet session is not connected");
        }
        return new TelnetExecutor(client, inputStream, outputStream, prompt, charset, readTimeout);
    }

    public TelnetExecutor getShellExecutor() {
        return this.getExecutor();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
        connected = false;
    }

    /**
     * @return 是否已连接
     */
    public boolean isConnected() {
        return connected && client.isConnected();
    }

    /**
     * @throws IOException IOException
     */
    private void login() throws IOException {
        if (Strings.isBlank(username)) {
            return;
        }
        LOGGER.info("TelnetSession-login start");
        if (Strings.isNotBlank(loginPrompt)) {
            // 读取登录提示
            readUntil(loginPrompt, readTimeout);
        }
        // 发送用户名
        writeLine(username);
        if (Strings.isNotBlank(password)) {
            if (Strings.isNotBlank(passwordPrompt)) {
                // 读取密码提示
                readUntil(passwordPrompt, readTimeout);
            }
            // 发送密码
            writeLine(password);
        }
        if (Strings.isNotBlank(prompt)) {
            // 读取命令提示符
            readUntil(prompt, readTimeout);
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
     * @param pattern pattern
     * @param timeout timeout
     * @return result
     * @throws IOException IOException
     */
    private String readUntil(String pattern, int timeout) throws IOException {
        if (Strings.isBlank(pattern)) {
            return Const.EMPTY;
        }
        int maxTimeout = timeout > 0 ? timeout : readTimeout;
        long startTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        int read;
        while ((read = inputStream.read()) != -1) {
            // 逐字节读取
            builder.append((char) read);
            if (endsWith(builder, pattern)) {
                break;
            }
            if (maxTimeout > 0 && System.currentTimeMillis() - startTime > maxTimeout) {
                throw Exceptions.timeout("telnet read timeout");
            }
            if (builder.length() > Const.BUFFER_KB_32) {
                throw Exceptions.runtime("telnet read buffer overflow");
            }
        }
        return builder.toString();
    }

    /**
     * @param builder builder
     * @param pattern pattern
     * @return result
     */
    private boolean endsWith(StringBuilder builder, String pattern) {
        int patternLength = pattern.length();
        int builderLength = builder.length();
        if (builderLength < patternLength) {
            return false;
        }
        int offset = builderLength - patternLength;
        for (int i = 0; i < patternLength; i++) {
            if (builder.charAt(offset + i) != pattern.charAt(i)) {
                return false;
            }
        }
        return true;
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
     * 关闭会话
     */
    @Override
    public void close() {
        Streams.close(inputStream);
        Streams.close(outputStream);
        if (client.isConnected()) {
            disconnect();
        }
    }

}
