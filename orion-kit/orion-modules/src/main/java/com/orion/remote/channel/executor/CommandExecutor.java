package com.orion.remote.channel.executor;

import com.jcraft.jsch.ChannelExec;
import com.orion.lang.thread.HookRunnable;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

/**
 * 命令执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/6 12:51
 */
public class CommandExecutor extends BaseExecutor {

    /**
     * channel
     */
    private ChannelExec channel;

    /**
     * 标准输入流
     */
    private InputStream inputStream;

    /**
     * 错误输入流
     */
    private InputStream errorStream;

    /**
     * 标准输出流
     */
    private OutputStream outputStream;

    /**
     * 是否合并标准输入流和错误输入流
     */
    private boolean inheritStream;

    /**
     * 命令输出处理器
     */
    private BiConsumer<CommandExecutor, InputStream> streamHandler;

    /**
     * 行处理器
     */
    private BiConsumer<CommandExecutor, String> lineHandler;

    /**
     * 行编码
     */
    private String lineCharset;

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

    public CommandExecutor(ChannelExec channel, String command) {
        this(channel, command.getBytes(StandardCharsets.UTF_8));
    }

    public CommandExecutor(ChannelExec channel, byte[] command) {
        super(channel);
        this.channel = channel;
        this.channel.setCommand(command);
        try {
            inputStream = this.channel.getInputStream();
            errorStream = this.channel.getErrStream();
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
    public CommandExecutor streamHandler(BiConsumer<CommandExecutor, InputStream> streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    /**
     * 设置行处理器
     *
     * @param lineHandler 行处理器
     * @return this
     */
    public CommandExecutor lineHandler(BiConsumer<CommandExecutor, String> lineHandler) {
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
    public CommandExecutor lineHandler(BiConsumer<CommandExecutor, String> lineHandler, String lineCharset) {
        this.lineHandler = lineHandler;
        this.lineCharset = lineCharset;
        return this;
    }

    /**
     * 合并输入流和输出流
     *
     * @return this
     */
    public CommandExecutor inherit() {
        inputStream = new SequenceInputStream(inputStream, errorStream);
        inheritStream = true;
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
        Streams.close(errorStream);
        Streams.close(outputStream);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public CommandExecutor write(String command) {
        return this.write(command.getBytes());
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public CommandExecutor write(String command, String charset) {
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
    public CommandExecutor write(byte[] command) {
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
    public CommandExecutor exit() {
        return this.write("exit 0".getBytes());
    }

    @Override
    public ChannelExec getChannel() {
        return channel;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public InputStream getErrorStream() {
        return errorStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public boolean isInheritStream() {
        return inheritStream;
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
