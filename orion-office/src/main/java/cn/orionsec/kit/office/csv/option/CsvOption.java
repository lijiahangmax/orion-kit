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
package cn.orionsec.kit.office.csv.option;

import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.office.csv.type.CsvEscapeMode;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * csv 配置项
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/22 15:08
 */
public class CsvOption implements Serializable {

    // -------------------- const start --------------------

    /**
     * 双文本限定符转义
     */
    public static final int ESCAPE_MODE_DOUBLED = CsvEscapeMode.DOUBLE_QUALIFIER.getMode();

    /**
     * 反斜杠转义
     */
    public static final int ESCAPE_MODE_BACKSLASH = CsvEscapeMode.BACKSLASH.getMode();

    /**
     * 初始化列数
     */
    public static final int INITIAL_COLUMN_COUNT = 10;

    /**
     * 初始化列缓冲区大小
     */
    public static final int INITIAL_COLUMN_BUFFER_SIZE = 50;

    /**
     * 复杂转义类型
     */
    public static final int UNICODE = 1;

    public static final int OCTAL = 2;

    public static final int DECIMAL = 3;

    public static final int HEX = 4;

    // -------------------- const end --------------------

    /**
     * 文本限定符
     */
    protected char textQualifier;

    /**
     * 是否使用文本限定符
     */
    protected boolean useTextQualifier;

    /**
     * 列边界符
     */
    protected char delimiter;

    /**
     * 行边界符
     */
    protected char lineDelimiter;

    /**
     * 注释符
     */
    protected char comment;

    /**
     * 转义类型
     * 1. 双文本限定符转义
     * 2. 反斜杠转义
     */
    protected int escapeMode;

    /**
     * 是否使用自定义行边界符 否则使用系统分隔符
     */
    protected boolean useCustomLineDelimiter;

    /**
     * 编码格式
     */
    protected Charset charset;

    /**
     * 是否去除首尾空格
     */
    protected boolean trim;

    public CsvOption() {
        this.textQualifier = Letters.QUOTE;
        this.useTextQualifier = true;
        this.delimiter = Letters.COMMA;
        this.lineDelimiter = Letters.NULL;
        this.comment = Letters.POUND;
        this.escapeMode = ESCAPE_MODE_DOUBLED;
        this.charset = StandardCharsets.UTF_8;
        this.trim = false;
    }

    protected CsvOption(CsvOption option) {
        this.textQualifier = option.textQualifier;
        this.useTextQualifier = option.useTextQualifier;
        this.delimiter = option.delimiter;
        this.lineDelimiter = option.lineDelimiter;
        this.useCustomLineDelimiter = option.useCustomLineDelimiter;
        this.comment = option.comment;
        this.escapeMode = option.escapeMode;
        this.charset = option.charset;
        this.trim = option.trim;
    }

    public char getTextQualifier() {
        return textQualifier;
    }

    public CsvOption setTextQualifier(char textQualifier) {
        this.textQualifier = textQualifier;
        return this;
    }

    public boolean isUseTextQualifier() {
        return useTextQualifier;
    }

    public CsvOption setUseTextQualifier(boolean useTextQualifier) {
        this.useTextQualifier = useTextQualifier;
        return this;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public CsvOption setDelimiter(char delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public char getLineDelimiter() {
        return lineDelimiter;
    }

    public CsvOption setLineDelimiter(char lineDelimiter) {
        this.useCustomLineDelimiter = lineDelimiter != Letters.NULL;
        this.lineDelimiter = lineDelimiter;
        return this;
    }

    public char getComment() {
        return comment;
    }

    public CsvOption setComment(char comment) {
        this.comment = comment;
        return this;
    }

    public int getEscapeMode() {
        return escapeMode;
    }

    public CsvOption setEscapeMode(int escapeMode) {
        this.escapeMode = escapeMode;
        return this;
    }

    public CsvOption setEscapeMode(CsvEscapeMode escapeMode) {
        this.escapeMode = escapeMode.getMode();
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public CsvOption setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public boolean isUseCustomLineDelimiter() {
        return useCustomLineDelimiter;
    }

    public boolean isTrim() {
        return trim;
    }

    public CsvOption setTrim(boolean trim) {
        this.trim = trim;
        return this;
    }

}
