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
package com.orion.office.csv.core;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.able.SafeFlushable;
import com.orion.lang.constant.Const;
import com.orion.lang.constant.Letters;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.office.csv.option.CsvOption;
import com.orion.office.csv.option.CsvWriterOption;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * csv 写入器 core
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/1 23:23
 */
public class CsvWriter implements SafeCloseable, SafeFlushable {

    private Writer writer;

    private boolean firstColumn = true;

    /**
     * 是否已关闭
     */
    private boolean closed;

    /**
     * 配置
     */
    private CsvWriterOption option;

    public CsvWriter(String file) {
        this(file, Letters.COMMA, StandardCharsets.UTF_8);
    }

    public CsvWriter(String file, char delimiter) {
        this(file, delimiter, StandardCharsets.UTF_8);
    }

    public CsvWriter(String file, char delimiter, Charset charset) {
        Valid.notBlank(file, "file can not be null");
        Valid.notNull(charset, "charset can not be null");
        this.option = new CsvWriterOption(delimiter, charset);
        this.writer = new BufferedWriter(new OutputStreamWriter(Files1.openOutputStreamSafe(file), charset));
    }

    public CsvWriter(String file, CsvWriterOption option) {
        Valid.notBlank(file, "file can not be null");
        Valid.notNull(option, "option not be null");
        Valid.notNull(option.getCharset(), "charset can not be null");
        this.option = option;
        this.writer = new BufferedWriter(new OutputStreamWriter(Files1.openOutputStreamSafe(file), option.getCharset()));
    }

    public CsvWriter(File file) {
        this(file, Letters.COMMA, StandardCharsets.UTF_8);
    }

    public CsvWriter(File file, char delimiter) {
        this(file, delimiter, StandardCharsets.UTF_8);
    }

    public CsvWriter(File file, char delimiter, Charset charset) {
        Valid.notNull(file, "file can not be null");
        Valid.notNull(charset, "charset can not be null");
        this.option = new CsvWriterOption(delimiter, charset);
        this.writer = new BufferedWriter(new OutputStreamWriter(Files1.openOutputStreamSafe(file), charset));
    }

    public CsvWriter(File file, CsvWriterOption option) {
        Valid.notNull(file, "file can not be null");
        Valid.notNull(option, "option can not be null");
        Valid.notNull(option.getCharset(), "charset can not be null");
        this.option = option;
        this.writer = new BufferedWriter(new OutputStreamWriter(Files1.openOutputStreamSafe(file), option.getCharset()));
    }

    public CsvWriter(OutputStream out) {
        this(new OutputStreamWriter(out, StandardCharsets.UTF_8), Letters.COMMA);
    }

    public CsvWriter(OutputStream out, char delimiter) {
        this(new OutputStreamWriter(out, StandardCharsets.UTF_8), delimiter);
    }

    public CsvWriter(OutputStream out, char delimiter, Charset charset) {
        this(new OutputStreamWriter(out, charset), delimiter);
    }

    public CsvWriter(OutputStream out, CsvWriterOption option) {
        this(new OutputStreamWriter(out, option.getCharset()), option);
    }

    public CsvWriter(Writer writer) {
        this(writer, Letters.COMMA);
    }

    public CsvWriter(Writer writer, char delimiter) {
        Valid.notNull(writer, "writer can not be null");
        this.writer = writer;
        this.option = new CsvWriterOption(delimiter);
    }

    public CsvWriter(Writer writer, CsvWriterOption option) {
        Valid.notNull(option, "option can not be null");
        Valid.notNull(writer, "writer can not be null");
        this.writer = writer;
        this.option = option;
    }

    /**
     * 写入列
     *
     * @param content 行
     * @throws IOException IOException
     */
    public void write(String content) throws IOException {
        write(content, !option.isTrim());
    }

    /**
     * 写入列
     *
     * @param content        行
     * @param preserveSpaces 是否保留空格
     * @throws IOException IOException
     */
    public void write(String content, boolean preserveSpaces) throws IOException {
        this.checkClosed();
        if (content == null) {
            content = Strings.EMPTY;
        }
        if (!firstColumn) {
            writer.write(option.getDelimiter());
        }
        boolean textQualify = option.isForceQualifier();
        if (!preserveSpaces && content.length() > 0) {
            content = content.trim();
        }

        if (!textQualify && option.isUseTextQualifier()
                && (content.indexOf(option.getTextQualifier()) > -1
                || content.indexOf(option.getDelimiter()) > -1
                || (!option.isUseCustomLineDelimiter() && (content.indexOf(Letters.LF) > -1 || content.indexOf(Letters.CR) > -1))
                || (option.isUseCustomLineDelimiter() && content.indexOf(option.getLineDelimiter()) > -1)
                || (firstColumn && content.length() > 0 && content.charAt(0) == option.getComment()) || (firstColumn && content.length() == 0))) {
            textQualify = true;
        }

        if (option.isUseTextQualifier() && !textQualify && content.length() > 0 && preserveSpaces) {
            char firstLetter = content.charAt(0);
            if (firstLetter == Letters.SPACE || firstLetter == Letters.TAB) {
                textQualify = true;
            }
            if (!textQualify && content.length() > 1) {
                char lastLetter = content.charAt(content.length() - 1);
                if (lastLetter == Letters.SPACE || lastLetter == Letters.TAB) {
                    textQualify = true;
                }
            }
        }

        if (textQualify) {
            writer.write(option.getTextQualifier());
            if (option.getEscapeMode() == CsvOption.ESCAPE_MODE_BACKSLASH) {
                content = replace(content, Strings.EMPTY + Letters.BACKSLASH, Strings.EMPTY + Letters.BACKSLASH + Letters.BACKSLASH);
                content = replace(content, Strings.EMPTY + option.getTextQualifier(), Strings.EMPTY + Letters.BACKSLASH + option.getTextQualifier());
            } else {
                content = replace(content, Strings.EMPTY + option.getTextQualifier(), Strings.EMPTY + option.getTextQualifier() + option.getTextQualifier());
            }
        } else if (option.getEscapeMode() == CsvOption.ESCAPE_MODE_BACKSLASH) {
            content = replace(content, Strings.EMPTY + Letters.BACKSLASH, Strings.EMPTY + Letters.BACKSLASH + Letters.BACKSLASH);
            content = replace(content, Strings.EMPTY + option.getDelimiter(), Strings.EMPTY + Letters.BACKSLASH + option.getDelimiter());

            if (option.isUseCustomLineDelimiter()) {
                content = replace(content, Strings.EMPTY + option.getLineDelimiter(), Strings.EMPTY + Letters.BACKSLASH + option.getLineDelimiter());
            } else {
                content = replace(content, Strings.EMPTY + Letters.CR, Strings.EMPTY + Letters.BACKSLASH + Letters.CR);
                content = replace(content, Strings.EMPTY + Letters.LF, Strings.EMPTY + Letters.BACKSLASH + Letters.LF);
            }

            if (firstColumn && content.length() > 0 && content.charAt(0) == option.getComment()) {
                if (content.length() > 1) {
                    content = Strings.EMPTY + Letters.BACKSLASH + option.getComment() + content.substring(1);
                } else {
                    content = Strings.EMPTY + Letters.BACKSLASH + option.getComment();
                }
            }
        }
        writer.write(content);
        if (textQualify) {
            writer.write(option.getTextQualifier());
        }
        firstColumn = false;
    }

    /**
     * 写入注释
     *
     * @param comment 注释
     * @throws IOException IOException
     */
    public void writeComment(String comment) throws IOException {
        this.checkClosed();
        writer.write(option.getComment());
        writer.write(comment);
        if (option.isUseCustomLineDelimiter()) {
            writer.write(option.getLineDelimiter());
        } else {
            writer.write(Const.LF);
        }
        firstColumn = true;
    }

    /**
     * 写入行
     *
     * @param values 行
     * @throws IOException IOException
     */
    public void writeLine(String[] values) throws IOException {
        this.writeLine(values, !option.isTrim());
    }

    /**
     * 写入行
     *
     * @param values         行
     * @param preserveSpaces 是否保留空格
     * @throws IOException IOException
     */
    public void writeLine(String[] values, boolean preserveSpaces) throws IOException {
        if (values != null && values.length > 0) {
            for (String value : values) {
                this.write(value, preserveSpaces);
            }
            this.newLine();
        }
    }

    /**
     * 设置行边界符
     *
     * @throws IOException IOException
     */
    public void newLine() throws IOException {
        this.checkClosed();
        if (option.isUseCustomLineDelimiter()) {
            writer.write(option.getLineDelimiter());
        } else {
            writer.write(Const.LF);
        }
        firstColumn = true;
    }

    /**
     * 缓冲区写入
     */
    @Override
    public void flush() {
        Streams.flush(writer);
    }

    /**
     * 检查是否已关闭
     */
    private void checkClosed() {
        if (closed) {
            throw Exceptions.ioRuntime("this instance already been closed");
        }
    }

    @Override
    public void close() {
        if (!closed) {
            Streams.close(writer);
            writer = null;
            closed = true;
        }
    }

    public void setOption(CsvWriterOption option) {
        this.option = option;
    }

    public CsvWriterOption getOption() {
        return option;
    }

    /**
     * 替换
     *
     * @param original 原数据
     * @param pattern  规则什么
     * @param replace  替换为什么
     * @return for new
     */
    private static String replace(String original, String pattern, String replace) {
        int len = pattern.length();
        int found = original.indexOf(pattern);
        if (found > -1) {
            StringBuilder sb = new StringBuilder();
            int start = 0;
            while (found != -1) {
                sb.append(original, start, found);
                sb.append(replace);
                start = found + len;
                found = original.indexOf(pattern, start);
            }
            sb.append(original.substring(start));
            return sb.toString();
        } else {
            return original;
        }
    }

}