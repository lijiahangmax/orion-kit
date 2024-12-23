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

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * csv reader 配置项
 *
 * @author Jiahang Li
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
