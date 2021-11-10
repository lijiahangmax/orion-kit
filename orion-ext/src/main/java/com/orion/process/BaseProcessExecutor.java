package com.orion.process;

import com.orion.able.Executable;
import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.collect.Lists;

import java.util.*;

/**
 * 命令处理器基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/12 15:59
 */
public abstract class BaseProcessExecutor implements Executable, SafeCloseable {

    /**
     * 系统命令
     */
    private static final ArrayList<String> COMMAND = new ArrayList<>();

    static {
        if (Systems.BE_WINDOWS) {
            COMMAND.add("cmd");
            COMMAND.add("/c");
        } else {
            COMMAND.add("/bin/bash");
            COMMAND.add("-c");
        }
    }

    /**
     * 命令
     */
    protected String[] command;

    /**
     * 命令执行文件夹
     */
    protected String dir;

    /**
     * 是否将异常流合并到标准流
     */
    protected boolean redirectError;

    /**
     * 当前环境变量
     */
    protected Map<String, String> env;

    /**
     * 新增环境变量
     */
    protected Map<String, String> addEnv;

    /**
     * 删除环境变量
     */
    protected List<String> removeEnv;

    protected BaseProcessExecutor(String command) {
        this(new String[]{command}, null);
    }

    protected BaseProcessExecutor(String[] command) {
        this(command, null);
    }

    protected BaseProcessExecutor(String command, String dir) {
        this(new String[]{command}, dir);
    }

    protected BaseProcessExecutor(String[] command, String dir) {
        this.command = command;
        this.dir = dir;
    }

    /**
     * 关闭
     */
    @Override
    public abstract void close();

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    public abstract boolean isAlive();

    /**
     * 获取 exit code
     *
     * @return -1未执行完毕 0成功 1失败
     */
    public abstract int getExitCode();

    /**
     * 获取进程
     *
     * @return Process
     */
    public abstract Process getProcess();

    /**
     * 获取进程构建器
     *
     * @return ProcessBuilder
     */
    public abstract ProcessBuilder getProcessBuilder();

    /**
     * 命名使用系统 terminal 执行
     * 如果进程不会自动停止不可以使用, 因为destroy杀死的不是terminal执行的进程, 而是terminal
     *
     * @return this
     */
    public BaseProcessExecutor terminal() {
        List<String> c = getTerminalCommand();
        for (String s : command) {
            c.add(s.replaceAll(Const.LF, Strings.SPACE).replaceAll(Const.CR, Strings.SPACE));
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
    public BaseProcessExecutor dir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * 合并错误流到标准流
     *
     * @return this
     */
    public BaseProcessExecutor redirectError() {
        this.redirectError = true;
        return this;
    }

    /**
     * 添加环境变量
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseProcessExecutor addEnv(String key, String value) {
        if (addEnv == null) {
            this.addEnv = new HashMap<>();
        }
        addEnv.put(key, value);
        return this;
    }

    /**
     * 添加环境变量
     *
     * @param env 环境变量
     * @return this
     */
    public BaseProcessExecutor addEnv(Map<String, String> env) {
        if (addEnv == null) {
            this.addEnv = new HashMap<>();
        }
        addEnv.putAll(env);
        return this;
    }

    /**
     * 删除环境变量
     *
     * @param keys key
     * @return this
     */
    public BaseProcessExecutor removeEnv(String... keys) {
        if (removeEnv == null) {
            this.removeEnv = new ArrayList<>();
        }
        if (keys != null) {
            removeEnv.addAll(Arrays.asList(keys));
        }
        return this;
    }

    /**
     * 删除环境变量
     *
     * @param keys key
     * @return this
     */
    public BaseProcessExecutor removeEnv(List<String> keys) {
        if (removeEnv == null) {
            this.removeEnv = new ArrayList<>();
        }
        if (keys != null) {
            removeEnv.addAll(keys);
        }
        return this;
    }

    /**
     * 获取系统命令前缀
     *
     * @return 前缀list
     */
    @SuppressWarnings("unchecked")
    protected static List<String> getTerminalCommand() {
        return (List<String>) COMMAND.clone();
    }

    public String[] getCommand() {
        return command;
    }

    public String getDir() {
        return dir;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public Map<String, String> getAddEnv() {
        return addEnv;
    }

    @Override
    public String toString() {
        return Lists.join(Lists.of(command), Strings.SPACE);
    }

}
