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
package com.orion.office.csv.reader;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.office.csv.core.CsvReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * csv array 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/2 18:48
 */
public class CsvArrayReader extends BaseCsvReader<String[]> {

    /**
     * 读取的列
     */
    protected int[] columns;

    /**
     * 列数
     */
    protected int columnSize;

    /**
     * 空列
     */
    private String columnEmpty;

    public CsvArrayReader(CsvReader reader) {
        this(reader, new ArrayList<>(), null);
    }

    public CsvArrayReader(CsvReader reader, Collection<String[]> rows) {
        this(reader, rows, null);
    }

    public CsvArrayReader(CsvReader reader, Consumer<String[]> consumer) {
        this(reader, null, consumer);
    }

    protected CsvArrayReader(CsvReader reader, Collection<String[]> rows, Consumer<String[]> consumer) {
        super(reader, rows, consumer);
    }

    /**
     * 设置列为空的值
     *
     * @param text text
     * @return this
     */
    public CsvArrayReader columnOfNull(String text) {
        this.columnEmpty = text;
        return this;
    }

    /**
     * 设置列为空的值为 ""
     *
     * @return this
     * @see Strings#EMPTY
     */
    public CsvArrayReader columnOfNullToEmpty() {
        this.columnEmpty = Strings.EMPTY;
        return this;
    }

    /**
     * 设置读取的列
     *
     * @param columns 列
     * @return this
     */
    public CsvArrayReader columns(int... columns) {
        this.columns = columns;
        this.columnSize = Arrays1.length(columns);
        return this;
    }

    /**
     * 设置列容量
     *
     * @param capacity capacity
     * @return this
     */
    public CsvArrayReader capacity(int capacity) {
        if (!Arrays1.isEmpty(columns)) {
            throw Exceptions.unsupported("if the column is set, the capacity is not supported");
        }
        this.columnSize = capacity;
        return this;
    }

    @Override
    protected String[] parserRow(String[] row) {
        if (Arrays1.isEmpty(columns) && (columnSize == 0 || row.length == columnSize)) {
            return row;
        }
        String[] array = new String[columnSize];
        for (int i = 0; i < columnSize; i++) {
            if (Arrays1.isEmpty(columns)) {
                array[i] = get(row, i, columnEmpty);
            } else {
                array[i] = get(row, columns[i], columnEmpty);
            }
        }
        return array;
    }

    public int getColumnSize() {
        return columnSize;
    }

}
