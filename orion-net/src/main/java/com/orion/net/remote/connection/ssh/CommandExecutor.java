package com.orion.net.remote.connection.ssh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.net.base.ssh.BaseCommandExecutor;

import java.io.IOException;
import java.io.SequenceInputStream;

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
    protected Session session;

    /**
     * 命令
     */
    private String command;

    /**
     * 命令编码
     */
    private String commandCharset;

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
        this.waitFor = ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS;
    }

    /**
     * 等待命令执行完毕
     *
     * @param type 等待类型
     */
    public void waitFor(int type) {
        this.waitFor = type;
    }

    /**
     * 等待命令执行完毕
     *
     * @param type    等待类型
     * @param timeout 超时时间
     */
    public void waitFor(int type, long timeout) {
        this.waitFor = type;
        this.waitTime = timeout;
    }

    /**
     * 命令超时时间
     *
     * @param timeout 超时时间
     */
    public void timeout(long timeout) {
        this.waitFor = ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS;
        this.waitTime = timeout;
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
                session.waitForCondition(waitFor, waitTime);
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

    public long getWaitTime() {
        return waitTime;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "[" + command + "]";
    }

}
