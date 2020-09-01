package com.orion.process;

import com.orion.able.Awaitable;
import com.orion.process.handler.ErrorHandler;
import com.orion.process.handler.StreamHandler;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 同步进程执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/17 13:44
 */
public class ProcessAwaitExecutor implements Awaitable<ProcessAwaitExecutor> {

    /**
     * 命令
     */
    private String[] command;

    /**
     * 命令执行文件夹
     */
    private String dir;

    /**
     * ProcessBuilder
     */
    private ProcessBuilder pb;

    /**
     * Process
     */
    private Process process;

    /**
     * 输入流
     */
    private InputStream in;

    /**
     * 错误流
     */
    private InputStream err;

    /**
     * 输出流
     */
    private OutputStream out;

    /**
     * 是否合并流到系统流
     */
    private boolean inheritIO;

    /**
     * 是否将异常流合并到标准流
     */
    private boolean redirectErr;

    /**
     * 是否等待命令执行
     */
    private long waitFor = -1L;

    /**
     * 异常信息
     */
    private Exception exception;

    /**
     * 流处理器
     */
    private StreamHandler streamHandler;

    /**
     * 当前环境
     */
    private Map<String, String> env;

    /**
     * 新增环境变量
     */
    private Map<String, String> addEnv;

    /**
     * 删除环境变量
     */
    private List<String> removeEnv;

    /**
     * 错误处理器
     */
    private ErrorHandler<ProcessAwaitExecutor> errorHandler;

    public ProcessAwaitExecutor(String command) {
        this(new String[]{command}, null);
    }

    public ProcessAwaitExecutor(String[] command) {
        this(command, null);
    }

    public ProcessAwaitExecutor(String command, String dir) {
        this(new String[]{command}, dir);
    }

    public ProcessAwaitExecutor(String[] command, String dir) {
        this.command = command;
        this.dir = dir;
    }

    /**
     * 命名使用系统 terminal 执行
     * 如果进程不会自动停止不可以使用, 因为destroy杀死的不是terminal执行的进程, 而是terminal
     *
     * @return this
     */
    public ProcessAwaitExecutor terminal() {
        List<String> c = EnvCommand.getCommand();
        for (String s : this.command) {
            c.add(s.replaceAll("\n", EnvCommand.SPACE).replaceAll("\r", EnvCommand.SPACE));
        }
        this.command = c.toArray(new String[0]);
        return this;
    }

    /**
     * 设置命令执行的文件夹
     *
     * @param dir 文件夹
     * @return this
     */
    public ProcessAwaitExecutor dir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * 添加环境变量
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ProcessAwaitExecutor addEnv(String key, String value) {
        if (this.addEnv == null) {
            this.addEnv = new HashMap<>();
        }
        this.addEnv.put(key, value);
        return this;
    }

    /**
     * 添加环境变量
     *
     * @param env 环境变量
     * @return this
     */
    public ProcessAwaitExecutor addEnv(Map<String, String> env) {
        if (this.addEnv == null) {
            this.addEnv = new HashMap<>();
        }
        this.addEnv.putAll(env);
        return this;
    }

    /**
     * 删除环境变量
     *
     * @param keys key
     * @return this
     */
    public ProcessAwaitExecutor removeEnv(String... keys) {
        if (this.removeEnv == null) {
            this.removeEnv = new ArrayList<>();
        }
        if (keys != null) {
            this.removeEnv.addAll(Arrays.asList(keys));
        }
        return this;
    }

    /**
     * 删除环境变量
     *
     * @param keys key
     * @return this
     */
    public ProcessAwaitExecutor removeEnv(List<String> keys) {
        if (this.removeEnv == null) {
            this.removeEnv = new ArrayList<>();
        }
        if (keys != null) {
            this.removeEnv.addAll(keys);
        }
        return this;
    }

    /**
     * #await 合并子流到系统流
     *
     * @return this
     */
    public ProcessAwaitExecutor inheritIO() {
        this.inheritIO = true;
        return this;
    }

    /**
     * 合并err流到标准流
     *
     * @return this
     */
    public ProcessAwaitExecutor redirectErr() {
        this.redirectErr = true;
        return this;
    }

    /**
     * 等待命令执行完毕
     *
     * @return this
     */
    public ProcessAwaitExecutor waitFor() {
        this.waitFor = 0L;
        return this;
    }

    /**
     * #await 是否等待命令执行完毕
     *
     * @param timeout 超时时间
     * @return this
     */
    public ProcessAwaitExecutor waitFor(long timeout) {
        this.waitFor = timeout;
        return this;
    }

    /**
     * #await 流处理器
     *
     * @param streamHandler streamHandler
     * @return this
     */
    public ProcessAwaitExecutor streamHandler(StreamHandler streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    /**
     * 错误处理器
     *
     * @param errorHandler errorHandler
     * @return this
     */
    public ProcessAwaitExecutor errorHandler(ErrorHandler<ProcessAwaitExecutor> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ProcessAwaitExecutor await() {
        try {
            this.pb = new ProcessBuilder(command);
            this.env = this.pb.environment();
            if (this.env != null && this.addEnv != null) {
                this.env.putAll(this.addEnv);
            }
            if (this.env != null && this.removeEnv != null) {
                for (String key : this.removeEnv) {
                    this.env.remove(key);
                }
            }
            this.pb.directory(this.dir == null ? null : new File(this.dir));
            if (this.inheritIO) {
                this.pb.inheritIO();
            }
            // 是否将错误流合并到输出流
            this.process = this.pb.redirectErrorStream(this.redirectErr).start();
            this.out = this.process.getOutputStream();
            this.in = this.process.getInputStream();
            this.err = this.process.getErrorStream();

            // 如果流不处理可能会阻塞
            if (this.streamHandler != null) {
                this.streamHandler.handler(this, this.in, this.err, this.out);
            }

            if (this.waitFor != -1) {
                if (this.waitFor != 0) {
                    this.process.waitFor(this.waitFor, TimeUnit.MILLISECONDS);
                } else {
                    this.process.waitFor();
                }
            }
        } catch (Exception e) {
            this.exception = e;
            if (this.errorHandler != null) {
                this.errorHandler.onError(this, e);
            }
        }
        return this;
    }

    /**
     * 键入命令
     *
     * @param bs bs
     * @throws IOException IOException
     */
    public void write(byte[] bs) throws IOException {
        this.out.write(bs);
        this.out.write('\n');
        this.out.flush();
    }

    /**
     * 关闭进程
     */
    public void close() {
        close(false);
    }

    /**
     * 关闭进程
     *
     * @param exit 是否键入exit
     */
    public void close(boolean exit) {
        try {
            if (exit && this.out != null) {
                try {
                    this.write("exit".getBytes());
                } catch (Exception e1) {
                    // ignore
                }
            }
            if (this.process != null) {
                this.process.destroy();
            }
            Streams.close(this.in);
            Streams.close(this.out);
            Streams.close(this.err);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    public boolean isAlive() {
        return this.process.isAlive();
    }

    /**
     * 获取exit code
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    public int getExitCode() {
        if (!this.process.isAlive()) {
            return this.process.exitValue();
        }
        return -1;
    }

    public String[] getCommand() {
        return command;
    }

    public String getDir() {
        return dir;
    }

    public ProcessBuilder getProcessBuilder() {
        return pb;
    }

    public Process getProcess() {
        return process;
    }

    public InputStream getIn() {
        return in;
    }

    public InputStream getErr() {
        return err;
    }

    public OutputStream getOut() {
        return out;
    }

    public boolean isInheritIO() {
        return inheritIO;
    }

    public boolean isRedirectErr() {
        return redirectErr;
    }

    public long getWaitFor() {
        return waitFor;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isError() {
        return exception != null;
    }

    public Map<String, String> getEnv() {
        return env;
    }

}
