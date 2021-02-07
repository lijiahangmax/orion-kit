package com.orion.csv.option;

import com.orion.csv.type.CsvEscapeMode;
import com.orion.utils.constant.Letters;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * CSV 配置项
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/22 15:08
 */
public class CsvOption implements Serializable {

    // -------------------- const start --------------------

    /**
     * 默认文件后缀
     */
    public static final String DEFAULT_SUFFIX = "csv";

    /**
     * 双文本限定符转义
     */
    public static final int ESCAPE_MODE_DOUBLED = CsvEscapeMode.DOUBLE_QUALIFIER.getMode();

    /**
     * 反斜杠转义
     */
    public static final int ESCAPE_MODE_BACKSLASH = CsvEscapeMode.BACKSLASH.getMode();

    /**
     * 缓冲区大小
     */
    public static final int MAX_BUFFER_SIZE = 1024;

    /**
     * 文件缓冲区大小
     */
    public static final int MAX_FILE_BUFFER_SIZE = 4 * 1024;

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

    public CsvOption() {
        textQualifier = Letters.QUOTE;
        useTextQualifier = true;
        delimiter = Letters.COMMA;
        lineDelimiter = Letters.NULL;
        comment = Letters.POUND;
        escapeMode = ESCAPE_MODE_DOUBLED;
        charset = StandardCharsets.UTF_8;
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

}
