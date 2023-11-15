package com.orion.net.base.ssh;

import com.orion.lang.able.Executable;
import com.orion.lang.able.SafeCloseable;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * 远程执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 14:32
 */
public interface IRemoteExecutor extends Executable, SafeCloseable {

    /**
     * 设置读取线程池
     *
     * @param scheduler pool
     */
    void scheduler(ExecutorService scheduler);

    /**
     * 异步完成回调
     *
     * @param callback 回调方法
     */
    void callback(Runnable callback);

    /**
     * 传输标准输出流到指定输出流
     *
     * @param out out
     * @throws IOException IOException
     */
    void transfer(OutputStream out) throws IOException;

    /**
     * 设置标准输出流处理器
     *
     * @param streamHandler 标准输入流处理器
     */
    void streamHandler(Consumer<InputStream> streamHandler);

    /**
     * 写入命令
     *
     * @param command command
     */
    void write(byte[] command);

    /**
     * 写入命令
     *
     * @param command command
     */
    default void write(String command) {
        this.write(Strings.bytes(command));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     */
    default void write(String command, String charset) {
        this.write(Strings.bytes(command, charset));
    }

    /**
     * 写入命令
     *
     * @param command command
     */
    default void writeLine(String command) {
        this.write(Strings.bytes(command + Const.LF));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     */
    default void writeLine(String command, String charset) {
        this.write(Strings.bytes(command + Const.LF, charset));
    }

    /**
     * 中断 键入 ctrl + c
     */
    default void interrupt() {
        this.write(new byte[]{3, 10});
    }

    /**
     * 挂起 键入 ctrl + x
     */
    default void hangUp() {
        this.write(new byte[]{24, 10});
    }

    /**
     * 退出 键入 exit 0
     */
    default void exit() {
        this.exit(0);
    }

    /**
     * 退出 键入 exit ?
     *
     * @param code code
     */
    default void exit(int code) {
        this.write(Strings.bytes("exit " + code + Const.LF));
    }

    /**
     * 获取标准输出流
     *
     * @return 标准输出流
     */
    InputStream getInputStream();

    /**
     * 获取标准输入流
     *
     * @return 标准输入流
     */
    OutputStream getOutputStream();

    /**
     * 是否为执行中
     *
     * @return isRun
     */
    boolean isRun();

    /**
     * 是否执行完成
     *
     * @return isDone
     */
    boolean isDone();

}
