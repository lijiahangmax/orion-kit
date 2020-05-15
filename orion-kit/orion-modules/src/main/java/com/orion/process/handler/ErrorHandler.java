package com.orion.process.handler;

/**
 * 错误处理器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 15:24
 */
@FunctionalInterface
public interface ErrorHandler<T> {

    /**
     * 错误处理
     *
     * @param p ProcessExecutor
     * @param e Exception
     */
    void onError(T p, Exception e);

}
