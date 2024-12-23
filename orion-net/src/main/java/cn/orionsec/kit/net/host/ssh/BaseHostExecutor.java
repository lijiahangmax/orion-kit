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
package cn.orionsec.kit.net.host.ssh;

import cn.orionsec.kit.lang.support.Attempt;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;
import com.jcraft.jsch.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * 远程执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 14:32
 */
public abstract class BaseHostExecutor<T extends Channel> implements IHostExecutor {

    protected final T channel;

    /**
     * 标准输出流
     */
    protected InputStream inputStream;

    /**
     * 标准输入流
     */
    protected OutputStream outputStream;

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

    public BaseHostExecutor(T channel) {
        this.channel = channel;
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
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void sendSignal(String signal) {
        try {
            channel.sendSignal(signal);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 监听 标准输出/错误输出
     */
    protected abstract void listenerOutput();

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
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public T getChannel() {
        return channel;
    }

}
