package com.orion.csv.writer;

import com.alibaba.fastjson.JSON;
import com.orion.able.SafeCloseable;
import com.orion.csv.core.CsvWriter;
import com.orion.csv.option.CsvWriterOption;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Csv 导出器 基类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/22 17:53
 */
public abstract class BaseCsvWriter<K, V> implements SafeCloseable {

    /**
     * writer
     */
    protected CsvWriter writer;

    /**
     * 是否去除首尾空格
     */
    protected boolean trim;

    /**
     * 是否跳过空行
     */
    protected boolean skipNullRows = true;

    /**
     * 默认值
     */
    protected Map<K, String> defaultValue = new HashMap<>();

    /**
     * 数组容量
     */
    protected int capacity = -1;

    protected int maxColumnIndex;

    /**
     * 映射
     * key: column
     * value: valueKey
     */
    protected Map<Integer, K> mapping = new TreeMap<>();

    public BaseCsvWriter(CsvWriter writer) {
        Valid.notNull(writer, "csv writer is null");
        this.writer = writer;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public BaseCsvWriter<K, V> skip() {
        try {
            writer.newLine();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 跳过一行
     *
     * @param i 行
     * @return this
     */
    public BaseCsvWriter<K, V> skip(int i) {
        for (int j = 0; j < i; j++) {
            skip();
        }
        return this;
    }

    /**
     * 去除空格
     *
     * @return this
     */
    public BaseCsvWriter<K, V> trim() {
        this.trim = true;
        return this;
    }

    /**
     * 设置数组容量
     *
     * @param capacity capacity
     * @return this
     */
    public BaseCsvWriter<K, V> capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 是否跳过
     * @return this
     */
    public BaseCsvWriter<K, V> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    /**
     * 默认值
     *
     * @param k     k
     * @param value value
     * @return this
     */
    public BaseCsvWriter<K, V> defaultValue(K k, String value) {
        defaultValue.put(k, value);
        return this;
    }

    /**
     * 映射
     *
     * @param k      valueKey
     * @param column column
     * @return this
     */
    public BaseCsvWriter<K, V> mapping(K k, int column) {
        return mapping(column, k);
    }

    /**
     * 映射
     *
     * @param column column
     * @param k      valueKey
     * @return this
     */
    public BaseCsvWriter<K, V> mapping(int column, K k) {
        maxColumnIndex = Math.max(maxColumnIndex, column);
        mapping.put(column, k);
        return this;
    }

    /**
     * 添加表头
     *
     * @param headers 表头
     * @return this
     */
    public BaseCsvWriter<K, V> headers(String... headers) {
        if (headers == null) {
            if (skipNullRows) {
                return this;
            } else {
                return skip();
            }
        }
        try {
            writer.writeLine(headers, !trim);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 添加行
     *
     * @param list 行
     * @return this
     */
    public BaseCsvWriter<K, V> addRows(List<V> list) {
        list.forEach(this::addRow);
        return this;
    }

    /**
     * 添加行
     *
     * @param row 行
     * @return this
     */
    public BaseCsvWriter<K, V> addRow(V row) {
        System.out.println(JSON.toJSONString(row));
        if (row == null) {
            if (skipNullRows) {
                return this;
            } else {
                return skip();
            }
        }
        String[] parseRow = this.parseRow(row);
        if (parseRow == null) {
            if (skipNullRows) {
                return this;
            } else {
                return skip();
            }
        }
        try {
            writer.writeLine(parseRow, !trim);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 添加备注
     *
     * @param comments comments
     * @return this
     */
    public BaseCsvWriter<K, V> addComments(List<String> comments) {
        comments.forEach(this::addComment);
        return this;
    }

    /**
     * 添加备注
     *
     * @param comment comment
     * @return this
     */
    public BaseCsvWriter<K, V> addComment(String comment) {
        try {
            writer.writeComment(comment);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 解析行
     *
     * @param row 行
     * @return String[]
     */
    protected abstract String[] parseRow(V row);

    public BaseCsvWriter<K, V> flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    @Override
    public void close() {
        writer.close();
    }

    public CsvWriter getWriter() {
        return writer;
    }

    public CsvWriterOption getOption() {
        return writer.getOption();
    }

    public int getCapacity() {
        return capacity;
    }

}
