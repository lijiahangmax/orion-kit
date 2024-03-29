package com.orion.net.host.ssh.command;

import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionHolder;
import com.orion.net.host.SessionStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 命令执行器工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 13:48
 */
public class CommandExecutors {

    private CommandExecutors() {
    }

    /**
     * 执行命令获取命令输出
     *
     * @param host     机器主机
     * @param username 用户名
     * @param password 密码
     * @param command  命令
     * @return 命令输出
     * @throws IOException IOException
     */
    public static String getCommandOutputResult(String host, String username, String password, String command) throws IOException {
        return getCommandOutputResult(host, 22, username, password, command);
    }

    /**
     * 执行命令获取命令输出
     *
     * @param host     机器主机
     * @param port     port
     * @param username 用户名
     * @param password 密码
     * @param command  命令
     * @return 命令输出
     * @throws IOException IOException
     */
    public static String getCommandOutputResult(String host, int port, String username, String password, String command) throws IOException {
        try (SessionStore session = SessionHolder.create()
                .getSession(host, port, username)
                .password(password)
                .connect();
             CommandExecutor executor = session.getCommandExecutor(command)) {
            return getCommandOutputResultString(executor);
        }
    }

    /**
     * 执行命令获取命令输出
     *
     * @param executor executor
     * @return result
     * @throws IOException IOException
     */
    public static String getCommandOutputResultString(ICommandExecutor executor) throws IOException {
        return new String(getCommandOutputResult(executor));
    }

    /**
     * 执行命令获取命令输出
     *
     * @param executor executor
     * @return result
     * @throws IOException IOException
     */
    public static byte[] getCommandOutputResult(ICommandExecutor executor) throws IOException {
        Valid.notNull(executor, "command executor is null");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // 执行命令
            execCommand(executor, out);
            return out.toByteArray();
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 执行命令
     *
     * @param executor executor
     * @throws IOException IOException
     */
    public static void execCommand(ICommandExecutor executor, OutputStream transfer) throws IOException {
        executor.transfer(transfer);
        executor.connect();
        executor.exec();
    }

}
