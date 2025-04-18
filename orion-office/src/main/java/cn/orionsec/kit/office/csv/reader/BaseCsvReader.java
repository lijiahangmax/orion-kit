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
package cn.orionsec.kit.office.csv.reader;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.option.CsvReaderOption;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * csv 读取器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/2 16:36
 */
public abstract class BaseCsvReader<T> implements SafeCloseable, Iterable<T> {

    /**
     * 读取器
     */
    protected final CsvReader reader;

    /**
     * 读取的行
     */
    protected int rowNum;

    /**
     * 是否已经读取完毕
     */
    protected boolean end;

    /**
     * 读取的记录
     */
    protected Collection<T> rows;

    /**
     * 读取的消费器
     */
    protected Consumer<T> consumer;

    /**
     * 是否存储数据
     */
    protected boolean store;

    protected BaseCsvReader(CsvReader reader, Collection<T> rows, Consumer<T> consumer) {
        this.reader = Valid.notNull(reader, "reader is null");
        if (rows == null && consumer == null) {
            throw Exceptions.argument("rows container or row consumer one of them must not be empty");
        }
        this.rows = rows;
        this.consumer = consumer;
        this.store = rows != null;
    }

    /**
     * csv 迭代器 不会存储也不会消费
     *
     * @return 迭代器
     */
    @Override
    public CsvReaderIterator<T> iterator() {
        return new CsvReaderIterator<>(this);
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public BaseCsvReader<T> skip() {
        try {
            reader.skipRecord();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public BaseCsvReader<T> skip(int i) {
        for (int s = 0; s < i; s++) {
            this.skip();
        }
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 是否跳过空行
     * @return this
     */
    public BaseCsvReader<T> skipEmptyRows(boolean skip) {
        reader.getOption().setSkipEmptyRows(skip);
        return this;
    }

    /**
     * 读取所有行
     *
     * @return this
     */
    public BaseCsvReader<T> read() {
        while (!end) {
            this.readRow();
        }
        return this;
    }

    /**
     * 读取多行
     *
     * @param i 行
     * @return this
     */
    public BaseCsvReader<T> read(int i) {
        for (int j = 0; j < i && !end; j++) {
            this.readRow();
        }
        return this;
    }

    /**
     * 读取一行
     */
    protected void readRow() {
        T row = this.nextRow();
        if (end) {
            return;
        }
        if (store) {
            rows.add(row);
        } else {
            consumer.accept(row);
        }
    }

    /**
     * 读取一行
     *
     * @return row
     */
    protected T nextRow() {
        if (end) {
            return null;
        }
        try {
            // 读取行
            boolean read = reader.readRow();
            if (!read) {
                this.end = true;
                return null;
            }
            rowNum++;
            // 解析行
            return this.parserRow(reader.getRow());
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 解析行
     *
     * @param row row
     * @return row
     */
    protected abstract T parserRow(String[] row);

    /**
     * 获取value
     *
     * @param row   row
     * @param index index
     * @return value
     */
    protected String get(String[] row, int index) {
        return this.get(row, index, null);
    }

    /**
     * 获取value
     *
     * @param row   row
     * @param index index
     * @param def   默认值
     * @return value
     */
    protected String get(String[] row, int index, String def) {
        if (index < row.length) {
            return row[index];
        } else {
            return def;
        }
    }

    @Override
    public void close() {
        reader.close();
    }

    /**
     * 清空读取的行
     *
     * @return this
     */
    public BaseCsvReader<T> clear() {
        if (store && rows != null) {
            rows.clear();
        }
        return this;
    }

    public CsvReader getReader() {
        return reader;
    }

    public CsvReaderOption getOption() {
        return reader.getOption();
    }

    /**
     * 获取raw
     *
     * @return value
     */
    public String getRaw() {
        return reader.getRawRow();
    }

    public Collection<T> getRows() {
        return rows;
    }

    /**
     * @return 读取的行数
     */
    public int getRowNum() {
        return rowNum;
    }

}
