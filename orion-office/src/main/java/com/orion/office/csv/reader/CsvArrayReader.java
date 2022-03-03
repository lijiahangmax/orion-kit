package com.orion.office.csv.reader;

import com.orion.office.csv.core.CsvReader;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

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
