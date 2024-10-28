/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.ansi;

import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 字符操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/29 12:19
 */
public class AnsiChar implements AnsiElement {

    private final String code;

    public AnsiChar(String code) {
        this.code = code;
    }

    public static AnsiChar insertLine() {
        return new AnsiChar(INSERT_LINE);
    }

    /**
     * 插入 N 行空行 光标移动到首行空行第一位
     *
     * @param line line
     * @return char
     */
    public static AnsiChar insertLine(int line) {
        return new AnsiChar(line + INSERT_LINE);
    }

    public static AnsiChar deleteLine() {
        return new AnsiChar(DELETE_LINE);
    }

    /**
     * 删除 N 行 光标移动到第一位
     *
     * @param line line
     * @return char
     */
    public static AnsiChar deleteLine(int line) {
        return new AnsiChar(line + DELETE_LINE);
    }

    public static AnsiChar insertColumns() {
        return new AnsiChar(INSERT_COLUMNS);
    }

    /**
     * 插入 N 列 光标不动
     *
     * @param columns columns
     * @return char
     */
    public static AnsiChar insertColumns(int columns) {
        return new AnsiChar(columns + INSERT_COLUMNS);
    }

    public static AnsiChar deleteColumns() {
        return new AnsiChar(DELETE_COLUMNS);
    }

    /**
     * 删除 N 列 光标不动
     *
     * @param columns columns
     * @return char
     */
    public static AnsiChar deleteColumns(int columns) {
        return new AnsiChar(columns + DELETE_COLUMNS);
    }

    public static AnsiChar insertBlankChars() {
        return new AnsiChar(INSERT_BLANK_CHARS);
    }

    /**
     * 当前光标后插入 N 个空字符 光标不动
     *
     * @param count count
     * @return char
     */
    public static AnsiChar insertBlankChars(int count) {
        return new AnsiChar(count + INSERT_BLANK_CHARS);
    }

    public static AnsiChar deleteChars() {
        return new AnsiChar(DELETE_CHARS);
    }

    /**
     * 删除当前光标后 N 个字符 光标不动
     *
     * @param chars chars
     * @return char
     */
    public static AnsiChar deleteChars(int chars) {
        return new AnsiChar(chars + DELETE_CHARS);
    }

    public static AnsiChar repeatChar() {
        return new AnsiChar(REPEAT_CHAR);
    }

    /**
     * 重复当前光标前的字符 N 次 光标跟随 (光标前需要是有效字符)
     *
     * @param chars chars
     * @return char
     */
    public static AnsiChar repeatChar(int chars) {
        return new AnsiChar(chars + REPEAT_CHAR);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code;
    }

}
