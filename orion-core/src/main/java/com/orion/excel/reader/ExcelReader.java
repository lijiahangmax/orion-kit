package com.orion.excel.reader;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.able.SafeCloseable;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel 读取器 基类
 * <p>
 * 使用迭代器不会存储也不会消费
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/4 17:09
 */
public abstract class ExcelReader<T> implements SafeCloseable, Iterator<T>, Iterable<T> {

    protected Workbook workbook;

    protected Sheet sheet;

    /**
     * 行迭代器
     */
    protected Iterator<Row> iterator;

    /**
     * 迭代器索引
     */
    protected int currentIndex;

    /**
     * 行索引
     */
    protected int rowIndex;

    /**
     * 读取的行
     */
    protected int rowNum;

    /**
     * 总行数
     */
    protected int lines;

    /**
     * 是否已经读取完毕
     */
    protected boolean end;

    /**
     * 是否跳过空行
     */
    protected boolean skipNullRows = true;

    /**
     * 是否清除空格
     *
     * @see String
     */
    protected boolean trim;

    /**
     * 是否为流式读取
     */
    protected boolean streaming;

    /**
     * 读取的列
     */
    protected int[] columns;

    /**
     * 列数
     */
    protected int columnSize;

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

    /**
     * 下一个元素
     */
    protected T next;

    protected ExcelReader(Workbook workbook, Sheet sheet, List<T> rows, Consumer<T> consumer) {
        Valid.notNull(workbook, "workbook is null");
        Valid.notNull(sheet, "sheet is null");
        if (rows == null && consumer == null) {
            throw Exceptions.argument("rows container or row consumer one of them must not be empty");
        }
        this.workbook = workbook;
        this.sheet = sheet;
        this.rows = rows;
        this.consumer = consumer;
        this.store = rows != null;
        this.streaming = sheet instanceof StreamingSheet;
        this.lines = sheet.getLastRowNum() + 1;
        this.iterator = sheet.rowIterator();
    }

    /**
     * 初始化 默认实现
     *
     * @return this
     */
    public ExcelReader<T> init() {
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public ExcelReader<T> skip() {
        rowIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public ExcelReader<T> skip(int i) {
        rowIndex += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 是否跳过空行
     * @return this
     */
    public ExcelReader<T> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    /**
     * 是否清除空格
     *
     * @return this
     */
    public ExcelReader<T> trim() {
        this.trim = true;
        return this;
    }

    /**
     * 重新计算所有公式
     *
     * @return this
     */
    public ExcelReader<T> recalculationFormula() {
        sheet.setForceFormulaRecalculation(true);
        return this;
    }

    /**
     * 设置读取的列
     *
     * @param columns 列
     * @return this
     */
    public ExcelReader<T> columns(int... columns) {
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
    public ExcelReader<T> capacity(int capacity) {
        if (!Arrays1.isEmpty(columns)) {
            throw Exceptions.unSupport("if the column is set, the capacity is not supported");
        }
        this.columnSize = capacity;
        return this;
    }

    /**
     * 读取所有行
     *
     * @return this
     */
    public ExcelReader<T> read() {
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
    public ExcelReader<T> read(int i) {
        for (int j = 0; j < i; j++) {
            this.readRow();
        }
        return this;
    }

    /**
     * 读取一行
     */
    protected void readRow() {
        T row = nextRow();
        if (row == null && skipNullRows) {
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
        if (!iterator.hasNext()) {
            end = true;
            return null;
        }
        Row row = iterator.next();
        if (currentIndex++ == rowIndex) {
            rowIndex++;
            return parserRow(row);
        }
        return nextRow();
    }

    /**
     * 解析行
     *
     * @param row row
     * @return row
     */
    protected abstract T parserRow(Row row);

    /**
     * 迭代器 不会存储也不会消费
     *
     * @return 迭代器
     */
    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (end) {
            return false;
        }
        next = nextRow();
        if (next == null && skipNullRows) {
            return hasNext();
        }
        return true;
    }

    @Override
    public T next() {
        rowNum++;
        return next;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
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

    /**
     * @return 总行数
     */
    public int getLines() {
        return lines;
    }

}
