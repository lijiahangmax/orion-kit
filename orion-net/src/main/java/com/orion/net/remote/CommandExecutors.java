package com.orion.net.remote;

import com.orion.net.base.ssh.BaseCommandExecutor;
import com.orion.net.remote.channel.ChannelConnector;
import com.orion.net.remote.channel.SessionHolder;
import com.orion.net.remote.channel.SessionStore;
import com.orion.net.remote.channel.ssh.CommandExecutor;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

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
     * 同步执行命令获取命令输出
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
     * 同步执行命令获取命令输出
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
        try (SessionStore session = SessionHolder.getSession(host, port, username)
                .password(password)
                .connect();
             CommandExecutor executor = session.getCommandExecutor(command)) {
            return getCommandOutputResultString(executor);
        }
    }

    /**
     * 同步执行命令获取命令输出
     *
     * @param executor executor
     * @return result
     * @throws IOException IOException
     */
    public static String getCommandOutputResultString(BaseCommandExecutor executor) throws IOException {
        return new String(getCommandOutputResult(executor));
    }

    /**
     * 同步执行命令获取命令输出
     *
     * @param executor executor
     * @return result
     * @throws IOException IOException
     */
    public static byte[] getCommandOutputResult(BaseCommandExecutor executor) throws IOException {
        Valid.notNull(executor, "command executor is null");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            executor.sync();
            executor.transfer(out);
            if (executor instanceof ChannelConnector) {
                ((ChannelConnector) executor).connect();
            }
            executor.exec();
            return out.toByteArray();
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 异步执行命令获取命令输出
     *
     * @param executor executor
     * @throws IOException IOException
     */
    public static void syncExecCommand(BaseCommandExecutor executor, OutputStream transfer) throws IOException {
        executor.inherit();
        executor.sync();
        executor.transfer(transfer);
        if (executor instanceof ChannelConnector) {
            ((ChannelConnector) executor).connect();
        }
        executor.exec();
    }

}
