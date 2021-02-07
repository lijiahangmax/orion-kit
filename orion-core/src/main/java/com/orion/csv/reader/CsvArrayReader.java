package com.orion.csv.reader;

import com.orion.csv.core.CsvReader;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Csv Array 读取器
 *
 * @author ljh15
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

    /**
     * 空行
     */
    private String[] emptyArray;

    public CsvArrayReader(CsvReader reader) {
        this(reader, new ArrayList<>(), null);
    }

    public CsvArrayReader(CsvReader reader, List<String[]> rows) {
        this(reader, rows, null);
    }

    public CsvArrayReader(CsvReader reader, Consumer<String[]> consumer) {
        this(reader, null, consumer);
    }

    protected CsvArrayReader(CsvReader reader, List<String[]> rows, Consumer<String[]> consumer) {
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
     * 空行返回 长度为0的空数组
     *
     * @return this
     */
    public CsvArrayReader rowOfNullToEmpty() {
        this.emptyArray = new String[0];
        return this;
    }

    /**
     * 空行返回 长度为length的空数组
     *
     * @param length 长度
     * @return this
     */
    public CsvArrayReader rowOfNullToEmpty(int length) {
        this.emptyArray = new String[length];
        return this;
    }

    /**
     * 空行返回 emptyArray
     *
     * @param emptyArray 数组
     * @return this
     */
    public CsvArrayReader rowOfNullToEmpty(String[] emptyArray) {
        this.emptyArray = emptyArray;
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
            throw Exceptions.unSupport("if the column is set, the capacity is not supported");
        }
        this.columnSize = capacity;
        return this;
    }

    @Override
    protected String[] parserRow(String[] row) {
        if (row == null) {
            return emptyArray;
        }
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
