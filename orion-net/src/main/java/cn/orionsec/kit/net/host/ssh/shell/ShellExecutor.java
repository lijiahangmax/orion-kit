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
package cn.orionsec.kit.net.host.ssh.shell;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.net.host.ssh.BaseHostExecutor;
import cn.orionsec.kit.net.host.ssh.TerminalType;
import com.jcraft.jsch.ChannelShell;

import java.io.IOException;

/**
 * shell 执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 19:50
 */
public class ShellExecutor extends BaseHostExecutor<ChannelShell> implements IShellExecutor {

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

    public ShellExecutor(ChannelShell channel) {
        super(channel);
        this.terminalType = TerminalType.XTERM.getType();
        this.cols = 180;
        this.rows = 36;
        this.width = 1366;
        this.height = 768;
        try {
            this.inputStream = channel.getInputStream();
            this.outputStream = channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void x11Forward(boolean enable) {
        channel.setXForwarding(enable);
    }

    @Override
    public void setAgentForwarding(boolean enable) {
        channel.setAgentForwarding(enable);
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

    @Override
    public void resize() {
        try {
            channel.setPtySize(cols, rows, width, height);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void connect() {
        channel.setPtyType(terminalType, cols, rows, width, height);
        try {
            channel.connect();
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    @Override
    public void connect(int timeout) {
        channel.setPtyType(terminalType, cols, rows, width, height);
        try {
            channel.connect(timeout);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    @Override
    public void env(byte[] key, byte[] value) {
        channel.setEnv(key, value);
    }

    @Override
    public void env(String key, String value) {
        channel.setEnv(key, value);
    }

    @Override
    protected void listenerOutput() {
        try {
            // 监听输出流
            streamHandler.accept(inputStream);
        } finally {
            // 回调
            this.done = true;
            if (callback != null) {
                callback.run();
            }
        }
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("shell std output stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("channel is not connected");
        }
        // 监听输出流
        this.listenerOutput();
    }

    @Override
    public void close() {
        super.close();
        this.disconnectChannel();
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
