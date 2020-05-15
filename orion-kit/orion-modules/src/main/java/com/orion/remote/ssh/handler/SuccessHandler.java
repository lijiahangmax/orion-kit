package com.orion.remote.ssh.handler;

import com.orion.remote.ssh.CommandExecutor;

/**
 * 命令执行成功处理器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 14:18
 */
@FunctionalInterface
public interface SuccessHandler {

    /**
     * 执行成功处理器
     *
     * @param executor 执行器
     */
    void handler(CommandExecutor executor);

}
