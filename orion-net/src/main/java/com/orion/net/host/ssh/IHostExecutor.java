/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.net.host.ssh;

import com.orion.lang.able.Executable;
import com.orion.lang.able.SafeCloseable;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * 远程主机执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 14:32
 */
public interface IHostExecutor extends Executable, Runnable, SafeCloseable {

    /**
     * 执行任务 线程方式
     */
    @Override
    default void run() {
        this.exec();
    }

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
     * 设置环境变量
     * 这里只支持设置 /etc/ssh/sshd_config AcceptEnv 的环境变量
     * 否则只能使用 export LANG="en_US"; 来设置
     *
     * @param key   key
     * @param value value
     */
    void env(String key, String value);

    /**
     * 设置环境变量
     * 这里只支持设置 /etc/ssh/sshd_config AcceptEnv 的环境变量
     * 否则只能使用 export LANG="en_US"; 来设置
     *
     * @param key   key
     * @param value value
     */
    void env(byte[] key, byte[] value);

    /**
     * 是否启用 x11forwarding
     *
     * @param enable 是否启用
     */
    void x11Forward(boolean enable);

    /**
     * 启用代理转发
     *
     * @param enable 是否启用
     */
    void setAgentForwarding(boolean enable);

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
        this.write(new byte[]{3});
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
     * 发送信号量
     *
     * @param signal 信号
     */
    void sendSignal(String signal);

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
     * 是否执行完成
     *
     * @return isDone
     */
    boolean isDone();

}
