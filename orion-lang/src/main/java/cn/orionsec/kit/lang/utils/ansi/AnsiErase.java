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
package cn.orionsec.kit.lang.utils.ansi;

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
        return new AnsiErase(AnsiConst.ERASE_DISPLAY_END);
    }

    /**
     * 从光标擦除到屏幕开头 光标不动
     *
     * @return erase
     */
    public static AnsiErase displayStart() {
        return new AnsiErase(AnsiConst.ERASE_DISPLAY_START);
    }

    /**
     * 擦除整个屏幕 光标移到左上角
     *
     * @return erase
     */
    public static AnsiErase display() {
        return new AnsiErase(AnsiConst.ERASE_DISPLAY_ALL);
    }

    /**
     * 从光标擦除到行尾 光标不动
     *
     * @return erase
     */
    public static AnsiErase lineEnd() {
        return new AnsiErase(AnsiConst.ERASE_LINE_END);
    }

    /**
     * 从光标擦除到行头 光标不动
     *
     * @return erase
     */
    public static AnsiErase lineStart() {
        return new AnsiErase(AnsiConst.ERASE_LINE_START);
    }

    /**
     * 擦除整行 光标不动
     *
     * @return erase
     */
    public static AnsiErase line() {
        return new AnsiErase(AnsiConst.ERASE_LINE_ALL);
    }

    /**
     * 从当前位置 向右擦除字符 光标不动
     *
     * @param character character
     * @return erase
     */
    public static AnsiErase character(int character) {
        return new AnsiErase(character + AnsiConst.ERASE_CHARACTER);
    }

    @Override
    public String toString() {
        return AnsiConst.CSI_PREFIX + code;
    }

}
