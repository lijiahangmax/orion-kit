package com.orion.process;

import com.orion.able.Asyncable;
import com.orion.process.handler.ErrorHandler;
import com.orion.utils.io.Files1;

import java.io.File;
import java.util.*;

/**
 * 异步进程执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 13:44
 */
@SuppressWarnings("ALL")
public class ProcessAsyncExecutor {

    /**
     * 命令
     */
    private String[] command;

    /**
     * 命令执行文件夹
     */
    private String dir;

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
     * 错误流转存文件
     */
    private File errFile;

    /**
     * 是否拼接输出到文件
     */
    private boolean errAppend;

    /**
     * 输出流转存文件
     */
    private File outputFile;

    /**
     * 是否拼接输出到文件
     */
    private boolean outAppend;

    /**
     * 是否将异常流合并到标准流
     */
    private boolean redirectErr;

    /**
     * 当前环境
     */
    private Map<String, String> env;

    /**
     * 新增环境变量
     */
    private Map<String, String> addEnv;

    /**
     * 删除环境变量
     */
    private List<String> removeEnv;

    /**
     * 异常信息
     */
    private Exception exception;

    /**
     * 错误处理器
     */
    private ErrorHandler<ProcessAsyncExecutor> errorHandler;

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
        this.command = command;
        this.dir = dir;
    }

    /**
     * 命名使用系统 terminal 执行
     * 如果进程不会自动停止不可以使用, 因为destroy杀死的不是terminal执行的进程, 而是terminal
     *
     * @return this
     */
    public ProcessAsyncExecutor terminal() {
        List<String> c = EnvCommand.getCommand();
        for (String s : this.command) {
            c.add(s.replaceAll("\n", EnvCommand.SPACE).replaceAll("\r", EnvCommand.SPACE));
        }
        this.command = c.toArray(new String[0]);
        return this;
    }

    /**
     * 设置命令执行的文件夹
     *
     * @param dir 文件夹
     * @return this
     */
    public ProcessAsyncExecutor dir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * 添加环境变量
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ProcessAsyncExecutor addEnv(String key, String value) {
        if (this.addEnv == null) {
            this.addEnv = new HashMap<>();
        }
        this.addEnv.put(key, value);
        return this;
    }

    /**
     * 添加环境变量
     *
     * @param env 环境变量
     * @return this
     */
    public ProcessAsyncExecutor addEnv(Map<String, String> env) {
        if (this.addEnv == null) {
            this.addEnv = new HashMap<>();
        }
        this.addEnv.putAll(env);
        return this;
    }

    /**
     * 删除环境变量
     *
     * @param keys key
     * @return this
     */
    public ProcessAsyncExecutor removeEnv(String... keys) {
        if (this.removeEnv == null) {
            this.removeEnv = new ArrayList<>();
        }
        if (keys != null) {
            this.removeEnv.addAll(Arrays.asList(keys));
        }
        return this;
    }

    /**
     * 删除环境变量
     *
     * @param keys key
     * @return this
     */
    public ProcessAsyncExecutor removeEnv(List<String> keys) {
        if (this.removeEnv == null) {
            this.removeEnv = new ArrayList<>();
        }
        if (keys != null) {
            this.removeEnv.addAll(keys);
        }
        return this;
    }

    /**
     * #async 设置标准流文件
     *
     * @param inputFile 标准流文件
     * @return this
     */
    public ProcessAsyncExecutor inputFile(File inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    /**
     * #async 设置错误流文件
     *
     * @param errFile 错误流文件
     * @return this
     */
    public ProcessAsyncExecutor errFile(File errFile) {
        this.errFile = errFile;
        return this;
    }

    /**
     * #async 设置错误流文件
     *
     * @param errFile 错误流文件
     * @param append  是否拼接
     * @return this
     */
    public ProcessAsyncExecutor errFile(File errFile, boolean append) {
        this.errFile = errFile;
        this.errAppend = append;
        return this;
    }

    /**
     * #async 设置标准流输出的文件
     *
     * @param outputFile 标准流输出的文件
     * @return this
     */
    public ProcessAsyncExecutor outputFile(File outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    /**
     * #async 设置标准流输出的文件
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

    /**
     * 合并err流到标准流
     *
     * @return this
     */
    public ProcessAsyncExecutor redirectErr() {
        this.redirectErr = true;
        return this;
    }

    /**
     * 错误处理器
     *
     * @param errorHandler errorHandler
     * @return this
     */
    public ProcessAsyncExecutor errorHandler(ErrorHandler<ProcessAsyncExecutor> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public void async() {
        try {
            this.pb = new ProcessBuilder(command);
            this.env = this.pb.environment();
            if (this.env != null && this.addEnv != null) {
                this.env.putAll(this.addEnv);
            }
            if (this.env != null && this.removeEnv != null) {
                for (String key : this.removeEnv) {
                    this.env.remove(key);
                }
            }
            this.pb.directory(dir == null ? null : new File(dir));
            // 是否将错误流合并到输出流
            this.pb.redirectErrorStream(this.redirectErr);
            if (this.inputFile != null) {
                Files1.touch(this.inputFile);
                this.pb.redirectInput(this.inputFile);
            } else {
                this.pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
            }
            if (!this.redirectErr) {
                if (this.errFile != null) {
                    Files1.touch(this.errFile);
                    if (this.errAppend) {
                        this.pb.redirectError(ProcessBuilder.Redirect.appendTo(this.errFile));
                    } else {
                        this.pb.redirectError(ProcessBuilder.Redirect.to(this.errFile));
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
            this.exception = e;
            if (this.errorHandler != null) {
                this.errorHandler.onError(this, e);
            }
        }
    }

    /**
     * 关闭进程
     */
    public void close() {
        this.process.destroy();
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    public boolean isAlive() {
        return this.process.isAlive();
    }

    /**
     * 获取exit code
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    public int getExitCode() {
        if (!this.process.isAlive()) {
            return this.process.exitValue();
        }
        return -1;
    }

    public String[] getCommand() {
        return command;
    }

    public String getDir() {
        return dir;
    }

    public ProcessBuilder getProcessBuilder() {
        return pb;
    }

    public Process getProcess() {
        return process;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getErrFile() {
        return errFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public boolean isRedirectErr() {
        return redirectErr;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isError() {
        return exception != null;
    }

    public Map<String, String> getEnv() {
        return env;
    }

}
