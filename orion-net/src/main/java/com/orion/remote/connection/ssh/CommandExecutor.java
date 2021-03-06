package com.orion.remote.connection.ssh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.constant.Const;
import com.orion.lang.thread.HookRunnable;
import com.orion.remote.ExitCode;
import com.orion.support.Attempt;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.function.Consumer;

/**
 * 抽象命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/15 22:26
 */
public class CommandExecutor extends BaseRemoteExecutor {

    /**
     * 命令
     */
    private String command;

    /**
     * 命令编码
     */
    private String commandCharset;

    /**
     * 是否合并错误流
     */
    private boolean inherit;

    /**
     * 是否等待命令执行, 且不包含
     * 1. 连接关闭
     * 2. 输出数据传送完毕
     * 3. 进程状态为退出
     * 4. 超时
     */
    private int waitFor;

    /**
     * 等待命令执行超时时间
     */
    private long waitTime;

    /**
     * 命令错误输出流
     */
    private InputStream errorStream;

    /**
     * 命令合并输出流
     */
    private InputStream inheritStream;

    /**
     * 错误输出流处理器
     */
    private Consumer<InputStream> errorStreamHandler;

    /**
     * 是否同步获取输出
     */
    private boolean sync;

    /**
     * 执行状态 0未开始 1执行中 2执行完毕 3执行失败
     */
    private volatile int runStatus;

    public CommandExecutor(Session session, String command) {
        this(session, command, Const.UTF_8);
    }

    public CommandExecutor(Session session, String command, String commandCharset) {
        super(session);
        this.command = command;
        this.commandCharset = commandCharset;
    }

    /**
     * 合并错误流到输入流
     *
     * @return this
     */
    public CommandExecutor inherit() {
        this.inherit = true;
        return this;
    }

    /**
     * 是否同步获取输出
     *
     * @return this
     */
    public CommandExecutor sync() {
        this.sync = true;
        return this;
    }

    /**
     * 使用伪终端
     * <p>
     * 如果使用 当程序关闭时 命令进程一起关闭
     * 如果不使用 当程序关闭时 命令进程可能不会一起关闭
     * <p>
     * 必须在 {@link #exec} 之前调用
     *
     * @return this
     */
    public CommandExecutor usePty() {
        try {
            session.requestDumbPTY();
        } catch (Exception e) {
            throw Exceptions.ioRuntime("set pty type error", e);
        }
        return this;
    }

    public CommandExecutor waitFor() {
        this.waitFor = ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS;
        return this;
    }

    public CommandExecutor waitFor(int type) {
        this.waitFor = type;
        return this;
    }

    /**
     * 等待命令执行完毕
     *
     * @param type    等待类型
     * @param timeout 超时时间
     * @return this
     */
    public CommandExecutor waitFor(int type, long timeout) {
        this.waitFor = type;
        this.waitTime = timeout;
        return this;
    }

    /**
     * 命令超时时间
     *
     * @param timeout 超时时间
     * @return this
     */
    public CommandExecutor timeout(long timeout) {
        this.waitFor = ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS;
        this.waitTime = timeout;
        return this;
    }

    /**
     * 设置错误输出流处理器
     *
     * @param errorStreamHandler 错误输出流处理器
     * @return this
     */
    public BaseRemoteExecutor errorStreamHandler(Consumer<InputStream> errorStreamHandler) {
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
     * 启动并且读取输出
     */
    @Override
    public void exec() {
        super.exec();
        try {
            if (commandCharset != null) {
                session.execCommand(command, commandCharset);
            } else {
                session.execCommand(command);
            }
        } catch (Exception e) {
            this.runStatus = 3;
            throw Exceptions.runtime("execute command error", e);
        }
        this.inputStream = new StreamGobbler(session.getStdout());
        this.errorStream = new StreamGobbler(session.getStderr());
        this.outputStream = session.getStdin();
        if (inherit) {
            this.inheritStream = new SequenceInputStream(inputStream, errorStream);
        }
        this.runStatus = 1;
        // read standard input & error stream
        this.listenerInputAndError();
        // wait
        try {
            if (waitFor != 0) {
                session.waitForCondition(waitFor, waitTime);
            }
            this.done = true;
            this.runStatus = 2;
        } catch (IOException e) {
            this.runStatus = 3;
            throw Exceptions.timeout(e);
        }
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

    /**
     * 关闭会话
     */
    @Override
    public void close() {
        this.close = true;
        Streams.close(outputStream);
        Streams.close(inputStream);
        Streams.close(errorStream);
        Streams.close(inheritStream);
        session.close();
    }

    public String getCommand() {
        return command;
    }

    /**
     * 0 执行成功
     * 1 执行失败
     * null 脚本没有exit code
     *
     * @return 脚本执行的exit code
     */
    public Integer getExitCode() {
        return session.getExitStatus();
    }

    /**
     * 是否正常退出
     *
     * @return ignore
     */
    public boolean isSuccessExit() {
        return ExitCode.SUCCESS.getCode().equals(session.getExitStatus());
    }

    public InputStream getErrorStream() {
        return errorStream;
    }

    public InputStream getInheritStream() {
        return inheritStream;
    }

    public boolean isInherit() {
        return inherit;
    }

    public int getWaitFor() {
        return waitFor;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public int getRunStatus() {
        return runStatus;
    }

    @Override
    public String toString() {
        return "[" + command + "]";
    }

}
