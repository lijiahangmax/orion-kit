/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.net.host.ssh.command;

import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.net.host.HostConnector;
import cn.orionsec.kit.net.host.ssh.ExitCode;
import cn.orionsec.kit.net.host.ssh.IHostExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 远程命令执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 15:29
 */
public interface ICommandExecutor extends IHostExecutor, HostConnector, TimeoutEndpoint {

    /**
     * 合并标准输出流和错误输出流
     */
    void merge();

    /**
     * 是否使用伪终端
     * <p>
     * 如果使用 当程序关闭时 命令进程一起关闭
     * 如果不使用 当程序关闭时 命令进程可能不会一起关闭
     * <p>
     * 必须在
     * {@link CommandExecutor#connect}
     * 之前调用
     *
     * @param enable 是否使用
     */
    void pty(boolean enable);

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
     * 设置超时时间
     *
     * @param timeout timeout
     * @param checker checker
     */
    void timeout(long timeout, TimeoutChecker<TimeoutEndpoint> checker);

    /**
     * 设置超时时间
     *
     * @param timeout timeout
     * @param unit    unit
     * @param checker checker
     */
    default void timeout(long timeout, TimeUnit unit, TimeoutChecker<TimeoutEndpoint> checker) {
        this.timeout(unit.toMillis(timeout), checker);
    }

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
     * 是否超时
     *
     * @return 是否超时
     */
    boolean isTimeout();

    /**
     * 获取执行的命令
     *
     * @return command
     */
    String getCommand();

    /**
     * 获取执行的命令 byte
     *
     * @return command
     */
    byte[] getCommandBytes();

    /**
     * 获取错误输出流
     *
     * @return 错误输出流
     */
    InputStream getErrorStream();

}
