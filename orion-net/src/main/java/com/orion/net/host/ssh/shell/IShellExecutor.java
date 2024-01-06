package com.orion.net.host.ssh.shell;

import com.orion.net.host.ssh.IHostExecutor;
import com.orion.net.host.ssh.TerminalType;

/**
 * shell 执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 10:42
 */
public interface IShellExecutor extends IHostExecutor {

    /**
     * 设置终端类型
     *
     * @param type type
     */
    default void terminalType(TerminalType type) {
        this.terminalType(type.getType());
    }

    /**
     * 设置终端类型
     *
     * @param terminalType terminalType
     */
    void terminalType(String terminalType);

    /**
     * 设置页面大小
     *
     * @param cols 行字数
     * @param rows 列数
     */
    void size(int cols, int rows);

    /**
     * 设置页面 dpi
     *
     * @param width  宽px
     * @param height 高px
     */
    void dpi(int width, int height);

    /**
     * 设置页面大小
     *
     * @param cols   行字数
     * @param rows   列数
     * @param width  宽px
     * @param height 高px
     */
    void size(int cols, int rows, int width, int height);

    /**
     * 告知服务器重新设置终端大小
     */
    void resize();

    /**
     * 获取设置的终端类型
     *
     * @return 终端类型
     */
    String getTerminalType();

    /**
     * 获取 行字数
     *
     * @return 行字数
     */
    int getCols();

    /**
     * 获取 列数
     *
     * @return 列数
     */
    int getRows();

    /**
     * 获取 宽px
     *
     * @return 宽px
     */
    int getWidth();

    /**
     * 获取 高px
     *
     * @return 高px
     */
    int getHeight();

}
