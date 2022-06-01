package com.orion.net.base.ssh;

import com.orion.net.remote.ExitCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * 远程命令执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 15:29
 */
public interface ICommandExecutor {

    /**
     * 合并标准输出流和错误输出流
     */
    void inherit();

    /**
     * 同步获取输出
     */
    void sync();

    /**
     * 是否使用伪终端
     * <p>
     * 如果使用 当程序关闭时 命令进程一起关闭
     * 如果不使用 当程序关闭时 命令进程可能不会一起关闭
     * <p>
     * 必须在
     * {@link com.orion.net.remote.channel.ssh.CommandExecutor#connect}
     * {@link com.orion.net.remote.connection.ssh.CommandExecutor#exec}
     * 之前调用
     *
     * @param use 是否使用
     */
    void pty(boolean use);

    /**
     * 设置错误输出流处理器
     *
     * @param errorStreamHandler 错误输出流处理器
     */
    void errorStreamHandler(Consumer<InputStream> errorStreamHandler);

    /**
     * 传输错误输出流到指定输出流
     *
     * @param out out
     * @throws IOException IOException
     */
    void transferError(OutputStream out) throws IOException;

    /**
     * 获取退出码
     *
     * @return 0正常结束
     */
    int getExitCode();

    /**
     * 是否正常退出
     *
     * @return 返回码是否为 0
     */
    default boolean isSuccessExit() {
        return ExitCode.isSuccess(this.getExitCode());
    }

    /**
     * 是否已合并标准输出流和错误输出流
     *
     * @return 是否合并
     */
    boolean isInherit();

    /**
     * 获取执行的命令
     *
     * @return command
     */
    String getCommand();

    /**
     * 获取合并输出流
     *
     * @return 合并输出流
     * @see #inherit
     * @see #isInherit
     */
    InputStream getInheritStream();

    /**
     * 获取错误输出流
     *
     * @return 错误输出流
     */
    InputStream getErrorStream();

}
