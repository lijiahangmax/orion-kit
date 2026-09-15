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
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * Telnet 执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public abstract class BaseTelnetExecutor implements ITelnetExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTelnetExecutor.class);

    protected final TelnetClient client;

    /**
     * 标准输出流
     */
    protected final InputStream inputStream;

    /**
     * 标准输入流
     */
    protected final OutputStream outputStream;

    /**
     * 标准输出流处理器
     */
    protected Consumer<InputStream> streamHandler;

    /**
     * 执行完毕回调
     */
    protected Runnable callback;

    /**
     * 是否执行完毕
     */
    protected volatile boolean done;

    /**
     * 是否正在监听输出流
     */
    protected volatile boolean streamReading;

    /**
     * 连接是否已断开
     */
    protected volatile boolean disconnected;

    /**
     * 提示符
     */
    protected String prompt;

    /**
     * 编码
     */
    protected String charset;

    /**
     * 读取超时时间 ms
     */
    protected int readTimeout;

    /**
     * 最大读取缓冲区字节数
     */
    protected int maxReadBuffer;

    public BaseTelnetExecutor(TelnetClient client,
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
        this.readTimeout = readTimeout;
        this.maxReadBuffer = Const.BUFFER_KB_32;
    }

    @Override
    public void prompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public void charset(String charset) {
        this.charset = charset;
    }

    @Override
    public void maxReadBuffer(int maxReadBuffer) {
        this.maxReadBuffer = maxReadBuffer;
    }

    @Override
    public void callback(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void transfer(OutputStream out) throws IOException {
        this.streamHandler = Attempt.rethrows(i -> {
            Streams.transfer(i, out);
        });
    }

    @Override
    public void streamHandler(Consumer<InputStream> streamHandler) {
        this.streamHandler = streamHandler;
    }

    @Override
    public void write(byte[] command) {
        if (outputStream == null) {
            throw Exceptions.state("telnet output stream is null");
        }
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            this.disconnected = true;
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public String readUntil(String pattern) throws IOException {
        return readUntil(pattern, readTimeout);
    }

    @Override
    public String readUntil(String pattern, int timeout) throws IOException {
        this.checkStreamReading();
        if (!this.isConnected()) {
            throw Exceptions.connection("telnet session is not connected");
        }
        // socket 读超时只在阻塞读取期间生效, 否则流式监听会被空闲超时打断
        client.setSoTimeout(timeout);
        try {
            return TelnetReads.readUntil(inputStream, pattern, charset, timeout, maxReadBuffer);
        } catch (IOException e) {
            this.disconnected = true;
            throw e;
        } finally {
            this.resetSoTimeout();
        }
    }

    @Override
    public String readUntilPrompt() throws IOException {
        return this.readUntil(prompt, readTimeout);
    }

    /**
     * 检查是否正在监听输出流
     */
    protected void checkStreamReading() {
        if (streamReading) {
            throw Exceptions.runtime("telnet output stream is reading");
        }
    }

    /**
     * 监听 标准输出
     */
    protected abstract void listenerOutput();

    /**
     * 关闭 socket 读超时
     */
    private void resetSoTimeout() {
        if (client == null || !client.isConnected()) {
            return;
        }
        try {
            client.setSoTimeout(0);
        } catch (Exception e) {
            // ignored
        }
    }

    @Override
    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (Exception e) {
            LOGGER.warn("telnet executor disconnect error", e);
        } finally {
            this.disconnected = true;
        }
    }

    @Override
    public void close() {
        Streams.close(inputStream);
        Streams.close(outputStream);
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isConnected() {
        return !disconnected && client != null && client.isConnected();
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public int getReadTimeout() {
        return readTimeout;
    }

    @Override
    public int getMaxReadBuffer() {
        return maxReadBuffer;
    }

    @Override
    public TelnetClient getClient() {
        return client;
    }

}
