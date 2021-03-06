package com.orion.remote.channel.ssh;

import com.jcraft.jsch.ChannelExec;
import com.orion.lang.thread.HookRunnable;
import com.orion.remote.ExitCode;
import com.orion.support.Attempt;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * 命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 12:51
 */
public class CommandExecutor extends BaseRemoteExecutor {

    /**
     * command
     */
    private byte[] command;

    /**
     * channel
     */
    private ChannelExec channel;

    /**
     * 命令错误输出流
     */
    private InputStream errorStream;

    /**
     * 命令合并输出流
     */
    private InputStream inheritStream;

    /**
     * 是否合并标准输入流和错误输入流
     */
    private boolean inherit;

    /**
     * 是否同步获取输出
     */
    private boolean sync;

    /**
     * 错误输出流处理器
     */
    private Consumer<InputStream> errorStreamHandler;

    public CommandExecutor(ChannelExec channel, String command) {
        this(channel, Strings.bytes(command, StandardCharsets.UTF_8));
    }

    public CommandExecutor(ChannelExec channel, String command, String charset) {
        this(channel, Strings.bytes(command, charset));
    }

    public CommandExecutor(ChannelExec channel, byte[] command) {
        super(channel);
        this.channel = channel;
        this.command = command;
        this.channel.setCommand(command);
        try {
            this.inputStream = channel.getInputStream();
            this.errorStream = channel.getErrStream();
            this.outputStream = channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        // 分配伪终端 当程序关闭时 命令进程一起关闭
        channel.setPty(true);
    }

    /**
     * 合并输入流和输出流
     *
     * @return this
     */
    public CommandExecutor inherit() {
        this.inherit = true;
        return this;
    }

    /**
     * 同步获取输出
     *
     * @return this
     */
    public CommandExecutor sync() {
        this.sync = true;
        return this;
    }

    /**
     * 是否使用伪终端
     * <p>
     * 如果使用 当程序关闭时 命令进程一起关闭
     * 如果不使用 当程序关闭时 命令进程可能不会一起关闭
     * <p>
     * 必须在 {@link #connect} 之前调用
     *
     * @param use 是否使用
     * @return this
     */
    public CommandExecutor pty(boolean use) {
        channel.setPty(use);
        return this;
    }

    /**
     * 设置错误输出流处理器
     *
     * @param errorStreamHandler 错误输出流处理器
     * @return this
     */
    public CommandExecutor errorStreamHandler(Consumer<InputStream> errorStreamHandler) {
        this.errorStreamHandler = errorStreamHandler;
        return this;
    }

    /**
     * 转流倒output
     *
     * @param out out
     * @return this
     * @throws IOException IOException
     */
    public BaseRemoteExecutor transferError(OutputStream out) throws IOException {
        this.errorStreamHandler = Attempt.rethrows(i -> {
            Streams.transfer(i, out);
        });
        return this;
    }

    /**
     * 读取输出
     */
    @Override
    public void exec() {
        super.exec();
        if (inherit) {
            this.inheritStream = new SequenceInputStream(inputStream, errorStream);
        }
        // read standard input & error stream
        this.listenerInputAndError();
    }

    /**
     * 关闭流
     */
    @Override
    public void close() {
        Streams.close(outputStream);
        Streams.close(inputStream);
        Streams.close(errorStream);
        Streams.close(inheritStream);
        super.disconnectChannel();
    }

    /**
     * 获取执行码
     *
     * @return 0正常结束
     */
    public int getExitCode() {
        return channel.getExitStatus();
    }

    /**
     * 是否正常退出
     *
     * @return ignore
     */
    public boolean isSuccessExit() {
        return ExitCode.SUCCESS.getCode() == channel.getExitStatus();
    }

    /**
     * 监听标准输出流和错误流
     */
    private void listenerInputAndError() {
        if (sync) {
            try {
                if (inherit) {
                    streamHandler.accept(inheritStream);
                } else {
                    streamHandler.accept(inputStream);
                }
                if (errorStreamHandler != null && !inherit) {
                    errorStreamHandler.accept(errorStream);
                }
            } finally {
                this.done = true;
                if (callback != null) {
                    callback.accept(this);
                }
            }
        } else {
            Runnable runnable = new HookRunnable(() -> {
                if (inherit) {
                    streamHandler.accept(inheritStream);
                } else {
                    streamHandler.accept(inputStream);
                }
                if (errorStreamHandler != null && !inherit) {
                    errorStreamHandler.accept(errorStream);
                }
            }, () -> {
                this.done = true;
                if (callback != null) {
                    callback.accept(this);
                }
            }, true);
            Threads.start(runnable, scheduler);
        }
    }

    @Override
    public ChannelExec getChannel() {
        return channel;
    }

    public boolean isInherit() {
        return inherit;
    }

    public byte[] getCommand() {
        return command;
    }

    public InputStream getInheritStream() {
        return inheritStream;
    }

    public InputStream getErrorStream() {
        return errorStream;
    }

    @Override
    public String toString() {
        return command == null ? "[]" : "[" + new String(command, StandardCharsets.UTF_8) + "]";
    }

}
