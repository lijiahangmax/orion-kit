package com.orion.process;

import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;

import java.io.File;

/**
 * 异步进程执行器
 *
 * @author ljh15
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
            this.env = this.pb.environment();
            if (this.addEnv != null) {
                this.env.putAll(this.addEnv);
            }
            if (this.removeEnv != null) {
                for (String key : this.removeEnv) {
                    this.env.remove(key);
                }
            }
            this.pb.directory(dir == null ? null : new File(dir));
            // 是否将错误流合并到输出流
            this.pb.redirectErrorStream(this.redirectError);
            if (this.inputFile != null) {
                Files1.touch(this.inputFile);
                this.pb.redirectInput(this.inputFile);
            } else {
                this.pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
            }
            if (!this.redirectError) {
                if (this.errorFile != null) {
                    Files1.touch(this.errorFile);
                    if (this.errAppend) {
                        this.pb.redirectError(ProcessBuilder.Redirect.appendTo(this.errorFile));
                    } else {
                        this.pb.redirectError(ProcessBuilder.Redirect.to(this.errorFile));
                    }
                } else {
                    this.pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                }
            }
            if (this.outputFile != null) {
                Files1.touch(this.outputFile);
                if (this.outAppend) {
                    this.pb.redirectOutput(ProcessBuilder.Redirect.appendTo(this.outputFile));
                } else {
                    this.pb.redirectOutput(ProcessBuilder.Redirect.to(this.outputFile));
                }
            } else {
                this.pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
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
        this.process.destroy();
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    @Override
    public boolean isAlive() {
        return this.process.isAlive();
    }

    /**
     * 获取exit code
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    @Override
    public int getExitCode() {
        if (!this.process.isAlive()) {
            return this.process.exitValue();
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
