package com.orion.net.remote.connection.ssh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.net.base.ssh.BaseCommandExecutor;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.concurrent.TimeUnit;

/**
 * 抽象命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/15 22:26
 */
public class CommandExecutor extends BaseCommandExecutor {

    /**
     * 会话
     */
    protected final Session session;

    /**
     * 命令
     */
    private final String command;

    /**
     * 命令编码
     */
    private final String commandCharset;

    /**
     * 是否等待命令执行
     * <p>
     * 1. 连接关闭
     * 2. 输出数据传送完毕
     * 3. 进程状态为退出
     * 4. 超时
     */
    private int waitFor;

    /**
     * 等待命令执行超时时间
     */
    private long timeout;

    public CommandExecutor(Session session, String command) {
        this(session, command, Const.UTF_8);
    }

    public CommandExecutor(Session session, String command, String commandCharset) {
        this.session = session;
        this.command = command;
        this.commandCharset = commandCharset;
    }

    @Override
    public void pty(boolean use) {
        if (!use) {
            return;
        }
        try {
            session.requestDumbPTY();
        } catch (Exception e) {
            throw Exceptions.ioRuntime("set pty type error", e);
        }
    }

    /**
     * 等待命令执行完毕
     */
    public void waitFor() {
        this.waitFor = ChannelCondition.TIMEOUT
                | ChannelCondition.CLOSED
                | ChannelCondition.EOF
                | ChannelCondition.EXIT_STATUS;
    }

    /**
     * 等待命令执行完毕
     *
     * @param type 等待类型
     */
    public void waitFor(int type) {
        this.waitFor = type;
    }

    @Override
    public void timeout(long timeout, TimeUnit unit) {
        this.timeout = unit.toMillis(timeout);
        if (this.waitFor == 0) {
            this.waitFor();
        }
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("command std output stream handler is null");
        }
        if (run) {
            throw Exceptions.runtime("this executor can only be executed once");
        }
        this.run = true;
        try {
            if (commandCharset != null) {
                session.execCommand(command, commandCharset);
            } else {
                session.execCommand(command);
            }
        } catch (Exception e) {
            throw Exceptions.runtime("execute command error", e);
        }
        this.inputStream = new StreamGobbler(session.getStdout());
        this.errorStream = new StreamGobbler(session.getStderr());
        this.outputStream = session.getStdin();
        if (inherit) {
            this.inheritStream = new SequenceInputStream(inputStream, errorStream);
        }
        // read standard input & error stream
        this.listenerStdoutAndError();
        // wait
        try {
            if (waitFor != 0) {
                session.waitForCondition(waitFor, timeout);
                this.done = true;
            }
        } catch (IOException e) {
            throw Exceptions.timeout(e);
        }
    }

    @Override
    public void close() {
        super.close();
        session.close();
    }

    @Override
    public int getExitCode() {
        return session.getExitStatus();
    }

    @Override
    public String getCommand() {
        return command;
    }

    public int getWaitFor() {
        return waitFor;
    }

    public long getTimeout() {
        return timeout;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public String toString() {
        return command;
    }

}
