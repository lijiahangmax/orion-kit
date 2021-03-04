package com.orion.office.excel.reader;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.able.SafeCloseable;
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
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/4 17:09
 */
public abstract class BaseExcelReader<T> implements SafeCloseable {

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

    protected BaseExcelReader(Workbook workbook, Sheet sheet, List<T> rows, Consumer<T> consumer) {
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
     * Excel 迭代器 不会存储也不会消费
     *
     * @return 迭代器
     */
    public ExcelReaderIterator<T> iterator() {
        return new ExcelReaderIterator<>(this);
    }

    /**
     * 初始化 默认实现
     *
     * @return this
     */
    public BaseExcelReader<T> init() {
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public BaseExcelReader<T> skip() {
        rowIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 行
     * @return this
     */
    public BaseExcelReader<T> skip(int i) {
        rowIndex += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skip 是否跳过空行
     * @return this
     */
    public BaseExcelReader<T> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    /**
     * 是否清除空格
     *
     * @return this
     */
    public BaseExcelReader<T> trim() {
        this.trim = true;
        return this;
    }

    /**
     * 重新计算所有公式
     *
     * @return this
     */
    public BaseExcelReader<T> recalculationFormula() {
        sheet.setForceFormulaRecalculation(true);
        return this;
    }

    /**
     * 读取所有行
     *
     * @return this
     */
    public BaseExcelReader<T> read() {
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
    public BaseExcelReader<T> read(int i) {
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
     * 清空读取的行
     *
     * @return this
     */
    public BaseExcelReader<T> clear() {
        if (store && this.rows != null) {
            this.rows.clear();
        }
        return this;
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
