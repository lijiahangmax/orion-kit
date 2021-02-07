package com.orion.csv.reader;

import com.orion.able.SafeCloseable;
import com.orion.csv.core.CsvReader;
import com.orion.csv.option.CsvReaderOption;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.util.List;
import java.util.function.Consumer;

/**
 * Csv 读取器 基类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/2 16:36
 */
public abstract class BaseCsvReader<T> implements SafeCloseable {

    /**
     * 读取器
     */
    protected CsvReader reader;

    /**
     * 读取的行
     */
    protected int rowNum;

    /**
     * 是否已经读取完毕
     */
    protected boolean end;

    /**
     * 是否跳过空行
     */
    protected boolean skipNullRows = true;

    /**
     * 读取的记录
     */
    protected List<T> rows;

    /**
     * 读取的消费器
     */
    protected Consumer<T> consumer;

    /**
     * 是否存储数据
     */
    protected boolean store;

    protected BaseCsvReader(CsvReader reader, List<T> rows, Consumer<T> consumer) {
        Valid.notNull(this.reader = reader, "reader is null");
        if (rows == null && consumer == null) {
            throw Exceptions.argument("rows container or row consumer one of them must not be empty");
        }
        this.rows = rows;
        this.consumer = consumer;
        this.store = rows != null;
    }

    /**
     * Csv 迭代器 不会存储也不会消费
     *
     * @return 迭代器
     */
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
            skip();
        }
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 是否跳过空行
     * @return this
     */
    public BaseCsvReader<T> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
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
        T row = nextRow();
        if (end || (row == null && skipNullRows)) {
            return;
        }
        if (store) {
            rows.add(row);
        } else {
            consumer.accept(row);
        }
        rowNum++;
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
            boolean read = reader.readRow();
            if (!read) {
                end = true;
                return null;
            }
            return parserRow(reader.getRow());
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
        return get(row, index, null);
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

    public List<T> getRows() {
        return rows;
    }

    /**
     * @return 读取的行数
     */
    public int getRowNum() {
        return rowNum;
    }

}
