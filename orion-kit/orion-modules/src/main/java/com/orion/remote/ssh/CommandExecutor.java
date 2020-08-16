package com.orion.remote.ssh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.able.Executorable;
import com.orion.remote.ssh.handler.ErrorHandler;
import com.orion.remote.ssh.handler.SuccessHandler;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Streams;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 抽象命令执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/15 22:26
 */
@SuppressWarnings("ALL")
public class CommandExecutor implements Executorable {

    /**
     * 会话
     */
    private Session session;

    /**
     * 命令
     */
    private String command;

    /**
     * 命令编码格式
     */
    private String commandCharset;

    /**
     * 是否合并错误流
     */
    private boolean inheritIO;

    /**
     * shell解析器类型
     */
    private String shellType;

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
     * 脚本执行的exit code
     * 0 执行成功
     * 1 执行失败
     * null 脚本没有exit code
     */
    private Integer exitCode;

    /**
     * 命令输入流
     */
    private OutputStream in;

    /**
     * 命令输出流
     */
    private InputStream out;

    /**
     * 命令错误输出流
     */
    private InputStream err;

    /**
     * 命令合并输出流
     */
    private InputStream inherit;

    /**
     * 执行状态 0未开始 1执行中 2执行完毕
     */
    private volatile int run;

    /**
     * 是否关闭
     */
    private boolean close;

    /**
     * 是否自动关闭shell 用于shellType不为空 且不需要键入其他命令使用
     */
    private boolean autoExitShell = true;

    /**
     * 异常
     */
    private Exception exception;

    /**
     * 执行成功处理器
     */
    private SuccessHandler successHandler;

    /**
     * 执行错误处理器
     */
    private ErrorHandler errorHandler;

    /**
     * 执行次数
     */
    private volatile AtomicInteger times = new AtomicInteger();

    public CommandExecutor(Session session, String command) {
        this.command = command;
        this.session = session;
    }

    public CommandExecutor(Session session, String command, String commandCharset) {
        this.command = command;
        this.session = session;
        this.commandCharset = commandCharset;
    }

    /**
     * 成功处理器
     *
     * @return this
     */
    public CommandExecutor successHandler(SuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    /**
     * 异常处理器
     *
     * @return this
     */
    public CommandExecutor errorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    /**
     * 合并错误流到输入流
     *
     * @return this
     */
    public CommandExecutor inheritIO() {
        this.inheritIO = true;
        return this;
    }

    /**
     * 使用bash解析器运行命令
     *
     * @return this
     */
    public CommandExecutor useBash() {
        this.shellType = "bash";
        return this;
    }

    /**
     * 使用解析器运行命令
     *
     * @return this
     */
    public CommandExecutor shellType(String shell) {
        this.shellType = shell;
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
     * 输入 用于shell类型命令时 并且 autoExitShell == false
     *
     * @param s 命令
     * @return this
     * @throws Exception Exception
     */
    public CommandExecutor write(String s) throws Exception {
        if (this.commandCharset == null) {
            this.in.write(s.getBytes());
        } else {
            this.in.write(s.getBytes(this.commandCharset));
        }
        this.in.write('\n');
        this.in.flush();
        return this;
    }

    /**
     * 设置是否自动退出shell
     *
     * @param auto auto
     * @return this
     */
    public CommandExecutor autoExitShell(boolean auto) {
        this.autoExitShell = auto;
        return this;
    }

    /**
     * 关闭shell
     *
     * @return this
     * @throws Exception Exception
     */
    public CommandExecutor exitShell() throws Exception {
        return this.write("exit 0");
    }


    @Override
    public void exec() {
        if (this.times.getAndIncrement() >= 1) {
            throw Exceptions.runtime("A thread can only be executed once");
        }
        try {
            if (this.shellType != null) {
                runShell();
            } else {
                runCommand();
            }
            if (this.successHandler != null) {
                this.successHandler.handler(this);
            }
        } catch (Exception e) {
            this.exception = e;
            if (this.errorHandler != null) {
                this.errorHandler.onError(this, e);
            }
        }
    }

    /**
     * 命令方式运行
     *
     * @throws Exception e
     */
    private void runCommand() throws Exception {
        if (this.commandCharset == null) {
            this.session.execCommand(this.command);
        } else {
            this.session.execCommand(this.command, this.commandCharset);
        }
        this.out = new StreamGobbler(session.getStdout());
        this.err = new StreamGobbler(session.getStderr());
        this.in = session.getStdin();
        if (this.inheritIO) {
            this.inherit = new SequenceInputStream(this.out, this.err);
        }
        this.run = 1;
        if (this.waitFor == 0 && this.waitTime != 0) {
            this.session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, this.waitTime);
        } else if (this.waitFor != 0) {
            this.session.waitForCondition(this.waitFor, this.waitTime);
        }
        this.exitCode = session.getExitStatus();
        this.run = 2;
    }

    /**
     * shell方式运行
     *
     * @throws Exception e
     */
    private void runShell() throws Exception {
        // 打开一个Shell
        this.session.requestPTY(this.shellType);
        this.session.startShell();
        this.out = new StreamGobbler(session.getStdout());
        this.err = new StreamGobbler(session.getStderr());
        if (this.inheritIO) {
            this.inherit = new SequenceInputStream(this.out, this.err);
        }
        // 输入命令
        this.in = this.session.getStdin();
        this.write(this.command);
        if (this.autoExitShell) {
            this.write("exit 0");
        }
        this.run = 1;
        if (this.waitFor == 0 && this.waitTime != 0) {
            this.session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, this.waitTime);
        } else if (this.waitFor != 0) {
            this.session.waitForCondition(this.waitFor, this.waitTime);
        }
        this.exitCode = session.getExitStatus();
        this.run = 2;
    }

    /**
     * 关闭会话
     */
    public void close() {
        this.close = true;
        Streams.close(this.in);
        Streams.close(this.out);
        Streams.close(this.err);
        Streams.close(this.inherit);
        this.session.close();
    }

    public Session getSession() {
        return session;
    }

    public String getCommand() {
        return command;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public OutputStream getIn() {
        return in;
    }

    public InputStream getOut() {
        return out;
    }

    public InputStream getErr() {
        return err;
    }

    public boolean isInheritIO() {
        return inheritIO;
    }

    public InputStream getInherit() {
        return inherit;
    }

    public String getCommandCharset() {
        return commandCharset;
    }

    public String getShellType() {
        return shellType;
    }

    public int getWaitFor() {
        return waitFor;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public boolean isAutoExitShell() {
        return autoExitShell;
    }

    public int getRun() {
        return run;
    }

    public boolean isError() {
        return this.exception != null;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isClose() {
        return close;
    }

}
