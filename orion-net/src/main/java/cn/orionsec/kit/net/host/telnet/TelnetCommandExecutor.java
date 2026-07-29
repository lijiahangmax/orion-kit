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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Telnet 命令执行器
 * <p>
 * 一次性命令执行器 发送命令后读取直到命中提示符
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public class TelnetCommandExecutor extends BaseTelnetExecutor implements ITelnetCommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetCommandExecutor.class);

    /**
     * 执行的命令
     */
    private final String command;

    /**
     * 是否保留命令回显和提示符
     */
    private boolean keepEcho;

    /**
     * @param client       client
     * @param inputStream  inputStream
     * @param outputStream outputStream
     * @param prompt       prompt
     * @param charset      charset
     * @param readTimeout  readTimeout
     * @param command      command
     */
    public TelnetCommandExecutor(TelnetClient client,
                                 InputStream inputStream,
                                 OutputStream outputStream,
                                 String prompt,
                                 String charset,
                                 int readTimeout,
                                 String command) {
        super(client, inputStream, outputStream, prompt, charset, readTimeout);
        this.command = command;
    }

    @Override
    public void keepEcho(boolean keepEcho) {
        this.keepEcho = keepEcho;
    }

    /**
     * 执行命令并将结果传输到输出流处理器
     */
    @Override
    public void exec() {
        Assert.notBlank(command, "command is blank");
        if (!this.isConnected()) {
            throw Exceptions.runtime("telnet session is not connected");
        }
        try {
            // 执行命令
            String result = this.execCommand(command, readTimeout);
            // 结果传输到输出流处理器
            if (streamHandler != null) {
                streamHandler.accept(new ByteArrayInputStream(Strings.bytes(result, charset)));
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            // 回调
            this.done = true;
            if (callback != null) {
                callback.run();
            }
        }
    }

    @Override
    public String execCommand(String command) throws IOException {
        return this.execCommand(command, readTimeout);
    }

    @Override
    public String execCommand(String command, int timeout) throws IOException {
        Assert.notBlank(command, "command is blank");
        LOGGER.info("TelnetCommandExecutor-execCommand send command {}", command);
        // 发送命令
        this.write(Strings.bytes(command + Const.LF, charset));
        // 读取直到提示符
        String result = this.readUntil(prompt, timeout);
        // 清理回显和提示符
        return this.cleanResult(result, command);
    }

    /**
     * 清理命令回显和末尾提示符
     *
     * @param result  result
     * @param command command
     * @return result
     */
    private String cleanResult(String result, String command) {
        if (keepEcho) {
            return result;
        }
        String cleaned = result;
        // 去除末尾提示符
        if (Strings.isNotBlank(prompt) && cleaned.endsWith(prompt)) {
            cleaned = cleaned.substring(0, cleaned.length() - prompt.length());
        }
        // 去除首行命令回显
        int firstLine = cleaned.indexOf(Const.LF);
        if (firstLine != -1 && cleaned.substring(0, firstLine).trim().equals(command.trim())) {
            cleaned = cleaned.substring(firstLine + 1);
        }
        return cleaned;
    }

    @Override
    protected void listenerOutput() {
        // 命令执行器为同步读取 无需监听输出流
    }

    @Override
    public String getCommand() {
        return command;
    }

}
