package com.orion.utils.awt;

import com.orion.utils.Exceptions;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * 剪切板工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/23 22:24
 */
public class Clipboards {

    private Clipboards() {
    }

    /**
     * 获取系统剪贴板
     *
     * @return Clipboard
     */
    public static Clipboard getClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * 设置内容到剪贴板
     *
     * @param contents 内容
     */
    public static void set(Transferable contents) {
        set(contents, null);
    }

    /**
     * 设置内容到剪贴板
     *
     * @param contents 内容
     * @param owner    所有者
     */
    public static void set(Transferable contents, ClipboardOwner owner) {
        getClipboard().setContents(contents, owner);
    }

    /**
     * 获取剪贴板内容
     *
     * @param flavor 数据元信息
     * @return 剪贴板内容
     */
    public static Object get(DataFlavor flavor) {
        return get(getClipboard().getContents(null), flavor);
    }

    /**
     * 获取剪贴板内容
     *
     * @param content Transferable
     * @param flavor  数据元信息
     * @return 剪贴板内容
     */
    public static Object get(Transferable content, DataFlavor flavor) {
        if (null != content && content.isDataFlavorSupported(flavor)) {
            try {
                return content.getTransferData(flavor);
            } catch (UnsupportedFlavorException | IOException e) {
                throw Exceptions.runtime(e);
            }
        }
        return null;
    }

    /**
     * 设置字符串文本到剪贴板
     *
     * @param text 字符串文本
     */
    public static void setString(String text) {
        set(new StringSelection(text));
    }

    /**
     * 从剪贴板获取文本
     *
     * @return 文本
     */
    public static String getString() {
        return (String) get(DataFlavor.stringFlavor);
    }

    /**
     * 从剪贴板的获取文本
     *
     * @param content 内容
     * @return 文本
     */
    public static String getString(Transferable content) {
        return (String) get(content, DataFlavor.stringFlavor);
    }

}
