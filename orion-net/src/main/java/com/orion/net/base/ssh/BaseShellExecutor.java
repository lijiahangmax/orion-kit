package com.orion.net.base.ssh;

import com.orion.lang.define.thread.HookRunnable;
import com.orion.lang.utils.Threads;
import com.orion.net.remote.TerminalType;

/**
 * shell 执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 10:48
 */
public abstract class BaseShellExecutor extends BaseRemoteExecutor implements IShellExecutor {

    /**
     * 终端类型
     */
    protected String terminalType;

    /**
     * 终端 行
     */
    protected int cols;

    /**
     * 终端 列
     */
    protected int rows;

    /**
     * 终端大小 宽 px
     */
    protected int width;

    /**
     * 终端大小 高 px
     */
    protected int height;

    public BaseShellExecutor() {
        this.terminalType = TerminalType.XTERM.getType();
        this.cols = 180;
        this.rows = 36;
        this.width = 1366;
        this.height = 768;
    }

    @Override
    public void terminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    @Override
    public void size(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
    }

    @Override
    public void dpi(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void size(int cols, int rows, int width, int height) {
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;
    }

    /**
     * 监听标准输出流
     */
    protected void listenerStdout() {
        Runnable runnable = new HookRunnable(() -> {
            streamHandler.accept(inputStream);
        }, () -> {
            this.done = true;
            if (callback != null) {
                callback.run();
            }
        }, true);
        Threads.start(runnable, scheduler);
    }

    @Override
    public String getTerminalType() {
        return terminalType;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
