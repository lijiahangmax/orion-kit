package com.orion.remote.connection.ssh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.able.ExecutorAble;
import com.orion.able.SafeCloseable;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.function.BiConsumer;

/**
 * 抽象命令执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/15 22:26
 */
public class CommandExecutor implements ExecutorAble, SafeCloseable {

    /**
     * 会话
     */
    private Session session;

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
     * 命令输入流
     */
    private OutputStream outputStream;

    /**
     * 命令输出流
     */
    private InputStream inputStream;

    /**
     * 命令错误输出流
     */
    private InputStream errorStream;

    /**
     * 命令合并输出流
     */
    private InputStream inheritStream;

    /**
     * 行处理器
     */
    private BiConsumer<CommandExecutor, String> lineHandler;

    /**
     * 行编码
     */
    private String lineCharset;

    /**
     * 命令输出处理器
     */
    private BiConsumer<CommandExecutor, InputStream> streamHandler;

    /**
     * 执行状态 0未开始 1执行中 2执行完毕 3执行失败
     */
    private volatile int runState;

    /**
     * 是否关闭
     */
    private boolean close;

    public CommandExecutor(Session session, String command) {
        this.session = session;
        this.command = command;
    }

    public CommandExecutor(Session session, String command, String commandCharset) {
        this.session = session;
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
        this.waitTime = timeout;
        return this;
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public CommandExecutor write(String command) {
        return this.write(Strings.bytes(command));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public CommandExecutor write(String command, String charset) {
        if (charset == null) {
            return this.write(Strings.bytes(command));
        } else {
            return this.write(Strings.bytes(command, charset));
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
        return this.write(Strings.bytes("exit 0"));
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
     * 启动并且读取输入
     */
    @Override
    public void exec() {
        if (lineHandler == null && streamHandler == null) {
            throw Exceptions.runtime("lineHandler and streamHandler is null");
        }
        if (runState != 0) {
            throw Exceptions.runtime("this executor can only be executed once");
        }
        try {
            if (this.commandCharset != null) {
                this.session.execCommand(this.command, this.commandCharset);
            } else {
                this.session.execCommand(this.command);
            }
        } catch (Exception e) {
            this.runState = 3;
            throw Exceptions.runtime("execute command error", e);
        }
        this.inputStream = new StreamGobbler(session.getStdout());
        this.errorStream = new StreamGobbler(session.getStderr());
        this.outputStream = session.getStdin();
        if (this.inherit) {
            this.inheritStream = new SequenceInputStream(this.inputStream, this.errorStream);
        }
        this.runState = 1;
        // read
        if (lineHandler != null) {
            Threads.start(() -> {
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
            });
        } else if (streamHandler != null) {
            Threads.start(() -> {
                streamHandler.accept(this, inputStream);
            });
        }
        // wait
        try {
            if (this.waitFor == 0 && this.waitTime != 0) {
                this.session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, this.waitTime);
            } else if (this.waitFor != 0) {
                this.session.waitForCondition(this.waitFor, this.waitTime);
            }
        } catch (IOException e) {
            this.runState = 3;
            throw Exceptions.timeout(e);
        }
        this.runState = 2;
    }

    /**
     * 关闭会话
     */
    @Override
    public void close() {
        this.close = true;
        Streams.close(this.outputStream);
        Streams.close(this.inputStream);
        Streams.close(this.errorStream);
        Streams.close(this.inheritStream);
        this.session.close();
    }

    public Session getSession() {
        return session;
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

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
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

    public int getRunState() {
        return runState;
    }

    public boolean isClose() {
        return close;
    }

    public boolean isDone() {
        return runState == 2 || runState == 3;
    }

}
