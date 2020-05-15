package com.orion.process.handler;

import com.orion.process.ProcessAwaitExecutor;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流处理器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 15:24
 */
@FunctionalInterface
public interface StreamHandler {

    /**
     * 流处理
     *
     * @param p   ProcessExecutor
     * @param in  标准流
     * @param err 错误流
     * @param out 输出流
     */
    void handler(ProcessAwaitExecutor p, InputStream in, InputStream err, OutputStream out);

}
