package com.orion.remote.ssh.handler;

import com.orion.remote.ssh.CommandExecutor;

/**
 * 命令执行错误处理
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 14:02
 */
@FunctionalInterface
public interface ErrorHandler {

    /**
     * 异常处理器
     *
     * @param executor 执行器
     * @param e        异常
     */
    void onError(CommandExecutor executor, Exception e);

}
