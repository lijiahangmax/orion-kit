package com.orion.office.csv.option;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * CSV Writer 配置项
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/22 15:18
 */
public class CsvWriterOption extends CsvOption implements Serializable {

    /**
     * 是否强制使用文本限定符 包裹文字
     */
    private boolean forceQualifier;

    public CsvWriterOption() {
        this.forceQualifier = false;
    }

    public CsvWriterOption(char delimiter) {
        this();
        super.delimiter = delimiter;
    }

    public CsvWriterOption(char delimiter, Charset charset) {
        this();
        super.delimiter = delimiter;
        super.charset = charset;
    }

    protected CsvWriterOption(CsvOption csvOption) {
        super(csvOption);
    }

    public boolean isForceQualifier() {
        return forceQualifier;
    }

    public CsvWriterOption setForceQualifier(boolean forceQualifier) {
        this.forceQualifier = forceQualifier;
        return this;
    }

    public CsvReaderOption toReaderOption() {
        return new CsvReaderOption(this);
    }

}
