package com.orion.lang.utils.ansi;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 擦除
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/28 17:40
 */
public class AnsiErase implements AnsiElement {

    private final String code;

    public AnsiErase(String code) {
        this.code = code;
    }

    /**
     * 从光标擦除直到屏幕结束 光标不动
     *
     * @return erase
     */
    public static AnsiErase displayEnd() {
        return new AnsiErase(ERASE_DISPLAY_END);
    }

    /**
     * 从光标擦除到屏幕开头 光标不动
     *
     * @return erase
     */
    public static AnsiErase displayStart() {
        return new AnsiErase(ERASE_DISPLAY_START);
    }

    /**
     * 擦除整个屏幕 光标移到左上角
     *
     * @return erase
     */
    public static AnsiErase display() {
        return new AnsiErase(ERASE_DISPLAY_ALL);
    }

    /**
     * 从光标擦除到行尾 光标不动
     *
     * @return erase
     */
    public static AnsiErase lineEnd() {
        return new AnsiErase(ERASE_LINE_END);
    }

    /**
     * 从光标擦除到行头 光标不动
     *
     * @return erase
     */
    public static AnsiErase lineStart() {
        return new AnsiErase(ERASE_LINE_START);
    }

    /**
     * 擦除整行 光标不动
     *
     * @return erase
     */
    public static AnsiErase line() {
        return new AnsiErase(ERASE_LINE_ALL);
    }

    /**
     * 从当前位置 向右擦除字符 光标不动
     *
     * @param character character
     * @return erase
     */
    public static AnsiErase character(int character) {
        return new AnsiErase(character + ERASE_CHARACTER);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code;
    }

}
