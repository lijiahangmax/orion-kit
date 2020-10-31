package com.orion.remote.connection.ssh;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.able.ExecutorAble;
import com.orion.able.SafeCloseable;
import com.orion.lang.thread.HookRunnable;
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
public class ShellExecutor implements ExecutorAble, SafeCloseable {

    /**
     * 会话
     */
    private Session session;

    /**
     * shell解析器类型
     */
    private String shellType;

    /**
     * 命令输入流
     */
    private OutputStream outputStream;

    /**
     * 命令输出流
     */
    private InputStream inputStream;

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
     * 是否关闭
     */
    private boolean close;

    /**
     * 是否执行完毕
     */
    private volatile boolean done;

    public ShellExecutor(Session session) {
        this(session, "bash");
    }

    public ShellExecutor(Session session, String shellType) {
        this.session = session;
        this.shellType = shellType;
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ShellExecutor write(String command) {
        return this.write(Strings.bytes(command));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public ShellExecutor write(String command, String charset) {
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
        return this.write(Strings.bytes("exit 0"));
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
     * 启动并且读取输入
     */
    @Override
    public void exec() {
        if (lineHandler == null && streamHandler == null) {
            throw Exceptions.runtime("lineHandler and streamHandler is null");
        }
        if (this.inputStream != null || this.outputStream != null) {
            throw Exceptions.runtime("this executor can only be executed once");
        }
        try {
            this.session.requestPTY(this.shellType);
            this.session.startShell();
        } catch (IOException e) {
            done = true;
            throw Exceptions.connection(e);
        }
        this.outputStream = session.getStdin();
        this.inputStream = new StreamGobbler(session.getStdout());
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
     * 关闭会话
     */
    @Override
    public void close() {
        this.close = true;
        Streams.close(this.outputStream);
        Streams.close(this.inputStream);
        this.session.close();
    }

    public Session getSession() {
        return session;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getShellType() {
        return shellType;
    }

    public boolean isClose() {
        return close;
    }

    public boolean isDone() {
        return done;
    }

}
