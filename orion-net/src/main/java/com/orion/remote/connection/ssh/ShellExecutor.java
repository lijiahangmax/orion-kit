package com.orion.remote.connection.ssh;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.orion.remote.TerminalType;
import com.orion.utils.Exceptions;

import java.io.IOException;

/**
 * 抽象命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/15 22:26
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

    public ShellExecutor(Session session) {
        super(session);
        this.terminalType = TerminalType.XTERM.getType();
        this.cols = 180;
        this.rows = 36;
        this.width = 1366;
        this.height = 768;
    }

    /**
     * 设置终端类型
     *
     * @param terminalType terminalType
     * @return this
     */
    public ShellExecutor terminalType(TerminalType terminalType) {
        this.terminalType = terminalType.getType();
        return this;
    }

    /**
     * 设置终端类型
     *
     * @param terminalType terminalType
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

    /**
     * 告知服务器重新设置终端大小
     *
     * @return this
     */
    public ShellExecutor resize() {
        try {
            session.requestWindowChange(cols, rows, width, height);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    @Override
    public void exec() {
        super.exec();
        try {
            session.requestPTY(terminalType, cols, rows, width, height, null);
            session.startShell();
        } catch (IOException e) {
            throw Exceptions.connection(e);
        }
        outputStream = session.getStdin();
        inputStream = new StreamGobbler(session.getStdout());
        super.listenerInput();
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
