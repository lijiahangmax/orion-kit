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
package cn.orionsec.kit.ext.process;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;

/**
 * 异步进程执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/17 13:44
 */
public class ProcessAsyncExecutor extends BaseProcessExecutor {

    /**
     * ProcessBuilder
     */
    private ProcessBuilder pb;

    /**
     * Process
     */
    private Process process;

    /**
     * 输入流读取文件
     */
    private File inputFile;

    /**
     * 输出流转存文件
     */
    private File outputFile;

    /**
     * 错误流转存文件
     */
    private File errorFile;

    /**
     * 是否拼接输出到文件
     */
    private boolean outAppend;

    /**
     * 是否拼接输出到文件
     */
    private boolean errAppend;

    public ProcessAsyncExecutor(String command) {
        this(new String[]{command}, null);
    }

    public ProcessAsyncExecutor(String[] command) {
        this(command, null);
    }

    public ProcessAsyncExecutor(String command, String dir) {
        this(new String[]{command}, dir);
    }

    public ProcessAsyncExecutor(String[] command, String dir) {
        super(command, dir);
    }

    /**
     * 设置标准流文件
     *
     * @param inputFile 标准流文件
     * @return this
     */
    public ProcessAsyncExecutor inputFile(String inputFile) {
        this.inputFile = new File(inputFile);
        return this;
    }

    /**
     * 设置标准流文件
     *
     * @param inputFile 标准流文件
     * @return this
     */
    public ProcessAsyncExecutor inputFile(File inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    /**
     * 设置错误流文件
     *
     * @param errorFile 错误流文件
     * @return this
     */
    public ProcessAsyncExecutor errorFile(String errorFile) {
        this.errorFile = new File(errorFile);
        return this;
    }

    /**
     * 设置错误流文件
     *
     * @param errorFile 错误流文件
     * @return this
     */
    public ProcessAsyncExecutor errorFile(File errorFile) {
        this.errorFile = errorFile;
        return this;
    }

    /**
     * 设置错误流文件
     *
     * @param errorFile 错误流文件
     * @param append    是否拼接
     * @return this
     */
    public ProcessAsyncExecutor errorFile(String errorFile, boolean append) {
        this.errorFile = new File(errorFile);
        this.errAppend = append;
        return this;
    }

    /**
     * 设置错误流文件
     *
     * @param errorFile 错误流文件
     * @param append    是否拼接
     * @return this
     */
    public ProcessAsyncExecutor errorFile(File errorFile, boolean append) {
        this.errorFile = errorFile;
        this.errAppend = append;
        return this;
    }

    /**
     * 设置标准流输出的文件
     *
     * @param outputFile 标准流输出的文件
     * @return this
     */
    public ProcessAsyncExecutor outputFile(String outputFile) {
        this.outputFile = new File(outputFile);
        return this;
    }

    /**
     * 设置标准流输出的文件
     *
     * @param outputFile 标准流输出的文件
     * @return this
     */
    public ProcessAsyncExecutor outputFile(File outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    /**
     * 设置标准流输出的文件
     *
     * @param outputFile 标准流输出的文件
     * @param append     是否拼接
     * @return this
     */
    public ProcessAsyncExecutor outputFile(String outputFile, boolean append) {
        this.outputFile = new File(outputFile);
        this.outAppend = append;
        return this;
    }

    /**
     * 设置标准流输出的文件
     *
     * @param outputFile 标准流输出的文件
     * @param append     是否拼接
     * @return this
     */
    public ProcessAsyncExecutor outputFile(File outputFile, boolean append) {
        this.outputFile = outputFile;
        this.outAppend = append;
        return this;
    }

    @Override
    public void exec() {
        try {
            this.pb = new ProcessBuilder(command);
            this.env = pb.environment();
            if (removeEnv != null) {
                for (String key : removeEnv) {
                    env.remove(key);
                }
            }
            if (addEnv != null) {
                env.putAll(addEnv);
            }
            pb.directory(dir == null ? null : new File(dir));
            // 是否将错误流合并到输出流
            pb.redirectErrorStream(redirectError);
            if (inputFile != null) {
                Files1.touch(inputFile);
                pb.redirectInput(inputFile);
            } else {
                pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
            }
            if (!redirectError) {
                if (errorFile != null) {
                    Files1.touch(errorFile);
                    if (errAppend) {
                        pb.redirectError(ProcessBuilder.Redirect.appendTo(errorFile));
                    } else {
                        pb.redirectError(ProcessBuilder.Redirect.to(errorFile));
                    }
                } else {
                    pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                }
            }
            if (outputFile != null) {
                Files1.touch(outputFile);
                if (outAppend) {
                    pb.redirectOutput(ProcessBuilder.Redirect.appendTo(outputFile));
                } else {
                    pb.redirectOutput(ProcessBuilder.Redirect.to(outputFile));
                }
            } else {
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            }
            this.process = pb.start();
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 关闭进程
     */
    @Override
    public void close() {
        process.destroy();
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    @Override
    public boolean isAlive() {
        return process.isAlive();
    }

    /**
     * 获取exit code
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    @Override
    public int getExitCode() {
        if (!process.isAlive()) {
            return process.exitValue();
        }
        return -1;
    }

    @Override
    public ProcessBuilder getProcessBuilder() {
        return pb;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getErrorFile() {
        return errorFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

}
