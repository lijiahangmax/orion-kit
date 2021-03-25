package com.orion.remote.channel.ssh;

import com.jcraft.jsch.ChannelShell;
import com.orion.utils.Exceptions;

import java.io.IOException;

/**
 * Shell执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 19:50
 */
public class ShellExecutor extends BaseRemoteExecutor {

    /**
     * 终端类型
     */
    private String terminalType;

    /**
     * 终端 行
     */
    private int cols;

    /**
     * 终端 列
     */
    private int rows;

    /**
     * 终端大小 宽 px
     */
    private int width;

    /**
     * 终端大小 高 px
     */
    private int height;

    /**
     * channel
     */
    private ChannelShell channel;

    public ShellExecutor(ChannelShell channel) {
        super(channel);
        this.channel = channel;
        this.terminalType = "xterm";
        this.cols = 180;
        this.rows = 36;
        this.width = 1366;
        this.height = 768;
        try {
            inputStream = this.channel.getInputStream();
            outputStream = this.channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置终端类型
     *
     * @param terminalType bash vt100 xterm
     * @return this
     */
    public ShellExecutor terminalType(String terminalType) {
        this.terminalType = terminalType;
        return this;
    }

    /**
     * 设置页面大小
     *
     * @param cols 行字数
     * @param rows 列数
     * @return this
     */
    public ShellExecutor size(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        return this;
    }

    /**
     * 设置页面dpi
     *
     * @param width  宽px
     * @param height 高px
     * @return this
     */
    public ShellExecutor dpi(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 设置页面大小
     *
     * @param cols   行字数
     * @param rows   列数
     * @param width  宽px
     * @param height 高px
     * @return this
     */
    public ShellExecutor size(int cols, int rows, int width, int height) {
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public ShellExecutor connect() {
        channel.setPtyType(terminalType, cols, rows, width, height);
        super.connect();
        return this;
    }

    @Override
    public ShellExecutor connect(int timeout) {
        channel.setPtyType(terminalType, cols, rows, width, height);
        super.connect(timeout);
        return this;
    }

    /**
     * 读取输出
     */
    @Override
    public void exec() {
        super.exec();
        super.listenerInput();
    }

    @Override
    public ChannelShell getChannel() {
        return channel;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
