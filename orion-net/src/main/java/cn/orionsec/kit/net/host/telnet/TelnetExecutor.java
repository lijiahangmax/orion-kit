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
import cn.orionsec.kit.lang.support.Attempt;
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
import java.util.function.Consumer;

/**
 * Telnet 执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public class TelnetExecutor implements ITelnetExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetExecutor.class);

    private final TelnetClient client;

    private InputStream inputStream;

    private OutputStream outputStream;

    private Consumer<InputStream> streamHandler;

    private Runnable callback;

    private volatile boolean done;

    private String prompt;

    private String charset;

    /**
     * 是否正在读取输出流
     */
    private volatile boolean isStreamReading;

    /**
     * @param client       client
     * @param inputStream  inputStream
     * @param outputStream outputStream
     * @param prompt       prompt
     * @param charset      charset
     * @param readTimeout  readTimeout
     */
    public TelnetExecutor(TelnetClient client,
                          InputStream inputStream,
                          OutputStream outputStream,
                          String prompt,
                          String charset,
                          int readTimeout) {
        this.client = client;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.prompt = prompt;
        this.charset = charset;
    }

    /**
     * @param prompt prompt
     */
    @Override
    public void prompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @return prompt
     */
    @Override
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param charset charset
     */
    @Override
    public void charset(String charset) {
        this.charset = charset;
    }

    /**
     * @return charset
     */
    @Override
    public String getCharset() {
        return charset;
    }

    /**
     * 检查是否正在读取输出流
     */
    private void checkStreamReading() {
        if (isStreamReading) {
            throw Exceptions.runtime("telnet output stream is reading");
        }
    }

    /**
     * @param callback callback
     */
    @Override
    public void callback(Runnable callback) {
        this.callback = callback;
    }

    /**
     * @param out out
     * @throws IOException IOException
     */
    @Override
    public void transfer(OutputStream out) throws IOException {
        this.streamHandler = Attempt.rethrows(i -> {
            Streams.transfer(i, out);
        });
    }

    /**
     * @param streamHandler streamHandler
     */
    @Override
    public void streamHandler(Consumer<InputStream> streamHandler) {
        this.streamHandler = streamHandler;
    }

    /**
     * @param command command
     */
    @Override
    public void write(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void connect() {
        if (!this.isConnected()) {
            throw Exceptions.connection("telnet session is not connected");
        }
    }

    /**
     * 监听输出流
     */
    protected void listenerOutput() {
        this.isStreamReading = true;
        try {
            // 读取输出流
            streamHandler.accept(inputStream);
        } finally {
            done = true;
            if (callback != null) {
                callback.run();
            }
        }
    }

    /**
     * 执行输出监听
     */
    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("telnet std output stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("telnet session is not connected");
        }
        if (inputStream == null || outputStream == null) {
            throw Exceptions.runtime("telnet stream is null");
        }
        // 触发输出监听
        listenerOutput();
    }

    /**
     * 关闭连接
     */
    @Override
    public void close() {
        Streams.close(inputStream);
        Streams.close(outputStream);
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * @return inputStream
     */
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * @return outputStream
     */
    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * @return done
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * @param pattern pattern
     * @return result
     * @throws IOException IOException
     */
    @Override
    public String readUntil(String pattern) throws IOException {
        return readUntil(pattern, 0);
    }

    /**
     * @param pattern pattern
     * @param timeout timeout
     * @return result
     * @throws IOException IOException
     */
    @Override
    public String readUntil(String pattern, int timeout) throws IOException {
        return doReadUntil(pattern, timeout);
    }

    /**
     * @return result
     * @throws IOException IOException
     */
    @Override
    public String readUntilPrompt() throws IOException {
        return doReadUntil(prompt, 0);
    }

    /**
     * @param command command
     * @return result
     * @throws IOException IOException
     */
    @Override
    public String execCommand(String command) throws IOException {
        return execCommand(command, 0);
    }

    /**
     * @param command command
     * @param timeout timeout
     * @return result
     * @throws IOException IOException
     */
    @Override
    public String execCommand(String command, int timeout) throws IOException {
        Assert.notBlank(command, "command is blank");
        LOGGER.info("TelnetExecutor-execCommand send command {}", command);
        // 发送命令
        this.write(Strings.bytes(command + Const.LF, charset));
        // 读取提示符
        return doReadUntil(prompt, timeout);
    }

    /**
     * @param pattern pattern
     * @param timeout timeout
     * @return result
     * @throws IOException IOException
     */
    private String doReadUntil(String pattern, int timeout) throws IOException {
        this.checkStreamReading();
        if (Strings.isBlank(pattern)) {
            return Const.EMPTY;
        }
        long startTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        int read;
        while ((read = inputStream.read()) != -1) {
            // 逐字节读取
            builder.append((char) read);
            if (endsWith(builder, pattern)) {
                break;
            }
            if (timeout > 0 && System.currentTimeMillis() - startTime > timeout) {
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

}
