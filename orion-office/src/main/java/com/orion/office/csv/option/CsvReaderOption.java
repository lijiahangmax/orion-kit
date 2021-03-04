package com.orion.office.csv.option;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * CSV Reader 配置项
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/22 15:12
 */
public class CsvReaderOption extends CsvOption implements Serializable {

    /**
     * 是否区分大小写
     */
    private boolean caseSensitive;

    /**
     * 是否读取注释
     */
    private boolean useComments;

    /**
     * 安全开关 如果行数过大 则会抛出异常 用于限制内存
     */
    private boolean safetySwitch;

    /**
     * 是否跳过空行 行长度为0
     */
    private boolean skipEmptyRows;

    /**
     * 是否跳过raw记录
     */
    private boolean skipRawRow;

    public CsvReaderOption() {
        this.caseSensitive = true;
        this.useComments = false;
        this.safetySwitch = true;
        this.skipEmptyRows = true;
        this.skipRawRow = false;
    }

    public CsvReaderOption(char delimiter) {
        this();
        super.delimiter = delimiter;
    }

    public CsvReaderOption(char delimiter, Charset charset) {
        this();
        super.delimiter = delimiter;
        super.charset = charset;
    }

    protected CsvReaderOption(CsvOption csvOption) {
        super(csvOption);
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public CsvReaderOption setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public boolean isUseComments() {
        return useComments;
    }

    public CsvReaderOption setUseComments(boolean useComments) {
        this.useComments = useComments;
        return this;
    }

    public boolean isSafetySwitch() {
        return safetySwitch;
    }

    public CsvReaderOption setSafetySwitch(boolean safetySwitch) {
        this.safetySwitch = safetySwitch;
        return this;
    }

    public boolean isSkipEmptyRows() {
        return skipEmptyRows;
    }

    public CsvReaderOption setSkipEmptyRows(boolean skipEmptyRows) {
        this.skipEmptyRows = skipEmptyRows;
        return this;
    }

    public boolean isSkipRawRow() {
        return skipRawRow;
    }

    public CsvReaderOption setSkipRawRow(boolean skipRawRow) {
        this.skipRawRow = skipRawRow;
        return this;
    }

    public CsvWriterOption toWriterOption() {
        return new CsvWriterOption(this);
    }

}
