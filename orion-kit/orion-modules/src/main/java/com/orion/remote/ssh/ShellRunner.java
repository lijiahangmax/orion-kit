package com.orion.remote.ssh;

import com.orion.exception.AuthenticationException;
import com.orion.remote.RemoteConnection;
import com.orion.remote.ssh.handler.ErrorHandler;
import com.orion.remote.ssh.handler.SuccessHandler;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * shell执行工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/14 22:29
 */
@SuppressWarnings("ALL")
public class ShellRunner {

    private ShellRunner() {
    }

    /**
     * shell执行线程池
     */
    private static final ExecutorService POOL = Threads.newThreadPool(0, 15, 30000, new LinkedBlockingQueue<>(), "SHELL-RUNNER-");

    /**
     * 获取远程连接
     *
     * @param host     host
     * @param username username
     * @param password password
     * @return RemoteConnection
     */
    public static RemoteConnection getRemoteConnection(String host, String username, String password) {
        RemoteConnection c;
        try {
            c = RemoteConnection.getConnection(host);
            if (!c.isAuth()) {
                c = new RemoteConnection(host).auth(username, password);
            }
            return c;
        } catch (AuthenticationException e) {
            throw Exceptions.runtime("Authentication SSH Server failed " + username + "@" + password);
        }
    }

    /**
     * shell 执行
     *
     * @param host         host
     * @param username     username
     * @param password     password
     * @param command      command
     * @param lineConsumer 行
     * @return CommandExecutor
     */
    public static CommandExecutor lineExecute(String host, String username, String password, String command, Consumer<String> lineConsumer) {
        return lineExecute(host, username, password, command, null, lineConsumer, null);
    }

    /**
     * shell 执行
     *
     * @param host         host
     * @param username     username
     * @param password     password
     * @param command      command
     * @param lineConsumer 行
     * @return CommandExecutor
     */
    public static CommandExecutor lineExecute(String host, String username, String password, String command, Consumer<String> lineConsumer, ErrorHandler errorHandler) {
        return lineExecute(host, username, password, command, null, lineConsumer, errorHandler);
    }

    /**
     * shell 执行
     *
     * @param host         host
     * @param username     username
     * @param password     password
     * @param command      command
     * @param charset      编码格式
     * @param lineConsumer 行
     * @return CommandExecutor
     */
    public static CommandExecutor lineExecute(String host, String username, String password, String command, String charset, Consumer<String> lineConsumer) {
        return lineExecute(host, username, password, command, charset, lineConsumer, null);
    }

    /**
     * shell 执行
     *
     * @param host         host
     * @param username     username
     * @param password     password
     * @param command      command
     * @param charset      编码格式
     * @param lineConsumer 行
     * @param errorHandler 错误处理器
     * @return CommandExecutor
     */
    public static CommandExecutor lineExecute(String host, String username, String password, String command, String charset, Consumer<String> lineConsumer, ErrorHandler errorHandler) {
        RemoteConnection c = getRemoteConnection(host, username, password);
        CommandExecutor executor = new CommandExecutor(c.getSession(), command)
                .inheritIO()
                .errorHandler(errorHandler)
                .successHandler((e) -> {
                    try {
                        BufferedReader reader;
                        if (charset == null) {
                            reader = new BufferedReader(new InputStreamReader(e.getInherit()));
                        } else {
                            reader = new BufferedReader(new InputStreamReader(e.getInherit(), charset));
                        }
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lineConsumer.accept(line);
                        }
                    } catch (Exception ex) {
                        throw Exceptions.ioRuntime(ex);
                    }
                });
        POOL.execute(executor::exec);
        return executor;
    }

    /**
     * shell 执行
     *
     * @param host     host
     * @param username username
     * @param password password
     * @param command  command
     * @param handler  成功处理器
     * @return CommandExecutor
     */
    public static CommandExecutor execute(String host, String username, String password, String command, SuccessHandler handler) {
        return execute(host, username, password, command, handler, null);
    }

    /**
     * shell 执行
     *
     * @param host         host
     * @param username     username
     * @param password     password
     * @param command      command
     * @param handler      成功处理器
     * @param errorHandler 错误处理器
     * @return CommandExecutor
     */
    public static CommandExecutor execute(String host, String username, String password, String command, SuccessHandler handler, ErrorHandler errorHandler) {
        RemoteConnection c = getRemoteConnection(host, username, password);
        CommandExecutor executor = new CommandExecutor(c.getSession(), command)
                .inheritIO()
                .successHandler(handler)
                .errorHandler(errorHandler);
        POOL.execute(executor::exec);
        return executor;
    }

}
