package com.orion.process;

import com.orion.lang.thread.HookRunnable;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 同步进程执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/17 13:44
 */
public class ProcessAwaitExecutor extends BaseProcessExecutor {

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
    private InputStream inputStream;

    /**
     * 错误流
     */
    private InputStream errorStream;

    /**
     * 输出流
     */
    private OutputStream outputStream;

    /**
     * 是否合并流到系统流
     */
    private boolean inherit;

    /**
     * 是否等待命令执行
     */
    private long waitFor = -1L;

    /**
     * 流处理器
     */
    private BiConsumer<ProcessAwaitExecutor, InputStream> streamHandler;

    /**
     * 行处理器
     */
    private BiConsumer<ProcessAwaitExecutor, String> lineHandler;

    /**
     * 行处理器编码
     */
    private String lineCharset;

    /**
     * 是否已完成
     */
    private volatile boolean done;

    /**
     * 是否已关闭
     */
    private boolean close;

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
        super(command, dir);
        this.streamHandler = streamHandler;
    }

    /**
     * 合并子流到系统流
     *
     * @return this
     */
    public ProcessAwaitExecutor inherit() {
        this.inherit = true;
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
     * 是否等待命令执行完毕
     *
     * @param timeout 超时时间
     * @return this
     */
    public ProcessAwaitExecutor waitFor(long timeout) {
        this.waitFor = timeout;
        return this;
    }

    /**
     * 设置命令输出处理器
     *
     * @param streamHandler 命令输出处理器
     * @return this
     */
    public ProcessAwaitExecutor streamHandler(BiConsumer<ProcessAwaitExecutor, InputStream> streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    /**
     * 设置行处理器
     *
     * @param lineHandler 行处理器
     * @return this
     */
    public ProcessAwaitExecutor lineHandler(BiConsumer<ProcessAwaitExecutor, String> lineHandler) {
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
    public ProcessAwaitExecutor lineHandler(BiConsumer<ProcessAwaitExecutor, String> lineHandler, String lineCharset) {
        this.lineHandler = lineHandler;
        this.lineCharset = lineCharset;
        return this;
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ProcessAwaitExecutor write(String command) {
        return this.write(Strings.bytes(command));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public ProcessAwaitExecutor write(String command, String charset) {
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
    public ProcessAwaitExecutor write(byte[] command) {
        try {
            this.outputStream.write(command);
            this.outputStream.write('\n');
            this.outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    @Override
    public void exec() {
        if (lineHandler == null && streamHandler == null) {
            throw Exceptions.runtime("lineHandler and streamHandler is null");
        }
        try {
            this.pb = new ProcessBuilder(command);
            this.env = this.pb.environment();
            if (this.addEnv != null) {
                this.env.putAll(this.addEnv);
            }
            if (this.removeEnv != null) {
                for (String key : this.removeEnv) {
                    this.env.remove(key);
                }
            }
            this.pb.directory(this.dir == null ? null : new File(this.dir));
            if (this.inherit) {
                this.pb.inheritIO();
            }
            // 是否将错误流合并到输出流
            this.process = this.pb.redirectErrorStream(this.redirectError).start();
            this.outputStream = this.process.getOutputStream();
            this.inputStream = this.process.getInputStream();
            this.errorStream = this.process.getErrorStream();

            // 如果流不处理可能会阻塞
            if (streamHandler != null) {
                Threads.start(new HookRunnable(() -> {
                    streamHandler.accept(this, this.inputStream);
                }, () -> {
                    this.done = true;
                }, true));
            } else if (lineHandler != null) {
                Threads.start(new HookRunnable(() -> {
                    try {
                        BufferedReader bufferedReader;
                        if (lineCharset == null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        } else {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, lineCharset));
                        }
                        String line;
                        while ((line = bufferedReader.readLine()) != null && !close) {
                            lineHandler.accept(this, line);
                        }
                    } catch (Exception e) {
                        throw Exceptions.ioRuntime(e);
                    }
                }, () -> {
                    this.done = true;
                }, true));
            }

            if (this.waitFor != -1) {
                if (this.waitFor != 0) {
                    this.process.waitFor(this.waitFor, TimeUnit.MILLISECONDS);
                } else {
                    this.process.waitFor();
                }
            }
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 关闭进程
     */
    @Override
    public void close() {
        close(false);
    }

    /**
     * 关闭进程
     *
     * @param exit 是否键入exit
     */
    public void close(boolean exit) {
        this.close = true;
        if (exit && this.outputStream != null) {
            try {
                this.write(Strings.bytes("exit 0"));
            } catch (Exception e1) {
                // ignore
            }
        }
        if (this.process != null) {
            this.process.destroy();
        }
        Streams.close(this.inputStream);
        Streams.close(this.outputStream);
        Streams.close(this.errorStream);
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    @Override
    public boolean isAlive() {
        return this.process.isAlive();
    }

    /**
     * 获取exit code
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    @Override
    public int getExitCode() {
        if (!this.process.isAlive()) {
            return this.process.exitValue();
        }
        return -1;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public ProcessBuilder getProcessBuilder() {
        return pb;
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

    public boolean isInherit() {
        return inherit;
    }

    public long getWaitFor() {
        return waitFor;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isClose() {
        return close;
    }

}
