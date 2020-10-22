package com.orion.remote.channel.executor;

import com.jcraft.jsch.ChannelShell;
import com.orion.lang.thread.HookRunnable;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.function.BiConsumer;

/**
 * Shell执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 19:50
 */
public class ShellExecutor extends BaseExecutor {

    /**
     * channel
     */
    private ChannelShell channel;

    /**
     * 标准输入流
     */
    private InputStream inputStream;

    /**
     * 标准输出流
     */
    private OutputStream outputStream;

    /**
     * 行处理器
     */
    private BiConsumer<ShellExecutor, String> lineHandler;

    /**
     * 行编码
     */
    private String lineCharset;

    /**
     * 命令输出处理器
     */
    private BiConsumer<ShellExecutor, InputStream> streamHandler;

    /**
     * 是否已运行
     */
    private boolean run;

    /**
     * 是否关闭
     */
    private boolean close;

    /**
     * 是否执行完毕
     */
    private volatile boolean done;

    public ShellExecutor(ChannelShell channel) {
        super(channel);
        this.channel = channel;
        try {
            inputStream = this.channel.getInputStream();
            outputStream = this.channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置命令输出处理器
     *
     * @param streamHandler 命令输出处理器
     * @return this
     */
    public ShellExecutor streamHandler(BiConsumer<ShellExecutor, InputStream> streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    /**
     * 设置行处理器
     *
     * @param lineHandler 行处理器
     * @return this
     */
    public ShellExecutor lineHandler(BiConsumer<ShellExecutor, String> lineHandler) {
        this.lineHandler = lineHandler;
        return this;
    }

    /**
     * 设置行处理器
     *
     * @param lineHandler 行处理器
     * @param lineCharset 行编码
     * @return this
     */
    public ShellExecutor lineHandler(BiConsumer<ShellExecutor, String> lineHandler, String lineCharset) {
        this.lineHandler = lineHandler;
        this.lineCharset = lineCharset;
        return this;
    }

    /**
     * 读取输入
     */
    @Override
    public void exec() {
        if (lineHandler == null && streamHandler == null) {
            throw Exceptions.runtime("lineHandler and streamHandler is null");
        }
        Valid.isTrue(isConnected(), "not connected");
        if (run) {
            throw Exceptions.runtime("A thread can only be executed once");
        }
        run = true;
        if (lineHandler != null) {
            Threads.start(new HookRunnable(() -> {
                try {
                    BufferedReader bufferedReader;
                    if (lineCharset == null) {
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, lineCharset));
                    }
                    String line;
                    while (!close && (line = bufferedReader.readLine()) != null) {
                        lineHandler.accept(this, line);
                    }
                } catch (Exception e) {
                    throw Exceptions.ioRuntime(e);
                }
            }, () -> {
                this.done = true;
            }, true));
        } else if (streamHandler != null) {
            Threads.start(new HookRunnable(() -> {
                streamHandler.accept(this, inputStream);
            }, () -> {
                this.done = true;
            }, true));
        }
    }

    /**
     * 关闭流
     */
    @Override
    public void close() {
        this.close = true;
        Streams.close(inputStream);
        Streams.close(outputStream);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ShellExecutor write(String command) {
        return this.write(command.getBytes());
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public ShellExecutor write(String command, String charset) {
        try {
            if (charset == null) {
                return this.write(command.getBytes());
            } else {
                return this.write(command.getBytes(charset));
            }
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding(e);
        }
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ShellExecutor write(byte[] command) {
        try {
            this.outputStream.write(command);
            this.outputStream.write('\n');
            this.outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 退出
     *
     * @return this
     */
    public ShellExecutor exit() {
        return this.write("exit 0".getBytes());
    }

    @Override
    public ChannelShell getChannel() {
        return channel;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public boolean isRun() {
        return run;
    }

    public boolean isClose() {
        return close;
    }

    public boolean isDone() {
        return done;
    }

}
