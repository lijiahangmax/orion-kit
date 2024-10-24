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
package com.orion.ext.process;

import com.orion.lang.support.Attempt;
import com.orion.lang.utils.io.Streams;

import java.io.ByteArrayOutputStream;

/**
 * process runtime 工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/31 9:46
 */
public class Processes {

    private Processes() {
    }

    public static String getOutputResultString(String... command) {
        return new String(getOutputResult(false, command));
    }

    public static String getOutputResultString(boolean redirectError, String... command) {
        return new String(getOutputResult(redirectError, command));
    }

    public static byte[] getOutputResult(String... command) {
        return getOutputResult(false, command);
    }

    /**
     * 获取输出结果
     * <p>
     * 适用于不阻塞结果的命令 如: echo
     * 可能会抛出运行时异常
     *
     * @param redirectError 合并错误流到输出流
     * @param command       command
     * @return result
     */
    public static byte[] getOutputResult(boolean redirectError, String... command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(command);
        try {
            if (redirectError) {
                executor.redirectError();
            }
            executor.streamHandler(i -> Attempt.uncheck(Streams::transfer, i, out))
                    .waitFor()
                    .sync()
                    .terminal()
                    .exec();
            return out.toByteArray();
        } finally {
            Streams.close(out);
            Streams.close(executor);
        }
    }

    public static String getOutputResultWithDirString(String dir, String... command) {
        return new String(getOutputResultWithDir(false, dir, command));
    }

    public static String getOutputResultWithDirString(boolean redirectError, String dir, String... command) {
        return new String(getOutputResultWithDir(redirectError, dir, command));
    }

    public static byte[] getOutputResultWithDir(String dir, String... command) {
        return getOutputResultWithDir(false, dir, command);
    }

    /**
     * 获取输出结果
     * <p>
     * 适用于不阻塞结果的命令
     *
     * @param redirectError 合并错误流到输出流
     * @param dir           执行文件夹
     * @param command       command
     * @return result
     */
    public static byte[] getOutputResultWithDir(boolean redirectError, String dir, String... command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(command);
        try {
            if (redirectError) {
                executor.redirectError();
            }
            executor.streamHandler(i -> Attempt.uncheck(Streams::transfer, i, out))
                    .waitFor()
                    .sync()
                    .dir(dir)
                    .exec();
            return out.toByteArray();
        } finally {
            Streams.close(out);
            Streams.close(executor);
        }
    }

}
