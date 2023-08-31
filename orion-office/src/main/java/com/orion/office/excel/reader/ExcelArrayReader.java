package com.orion.office.excel.reader;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * excel array 读取器
 * <p>
 * 不支持高级数据类型
 * <p>
 * {@link Excels#getCellValue(Cell)}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 10:04
 */
public class ExcelArrayReader extends BaseExcelReader<Integer, String[]> {

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

    public ExcelArrayReader(Workbook workbook, Sheet sheet) {
        this(workbook, sheet, new ArrayList<>(), null);
    }

    public ExcelArrayReader(Workbook workbook, Sheet sheet, List<String[]> store) {
        this(workbook, sheet, store, null);
    }

    public ExcelArrayReader(Workbook workbook, Sheet sheet, Consumer<String[]> consumer) {
        this(workbook, sheet, null, consumer);
    }

    private ExcelArrayReader(Workbook workbook, Sheet sheet, List<String[]> store, Consumer<String[]> consumer) {
        super(workbook, sheet, store, consumer);
    }

    public static ExcelArrayReader create(Workbook workbook, Sheet sheet) {
        return new ExcelArrayReader(workbook, sheet);
    }

    public static ExcelArrayReader create(Workbook workbook, Sheet sheet, List<String[]> store) {
        return new ExcelArrayReader(workbook, sheet, store);
    }

    public static ExcelArrayReader create(Workbook workbook, Sheet sheet, Consumer<String[]> consumer) {
        return new ExcelArrayReader(workbook, sheet, consumer);
    }

    /**
     * 设置列为空的值
     *
     * @param text text
     * @return this
     */
    public ExcelArrayReader columnOfNull(String text) {
        this.columnEmpty = text;
        return this;
    }

    /**
     * 设置列为空的值为 ""
     *
     * @return this
     * @see Strings#EMPTY
     */
    public ExcelArrayReader columnOfNullToEmpty() {
        this.columnEmpty = Strings.EMPTY;
        return this;
    }

    /**
     * 空行返回 长度为0的空数组
     *
     * @return this
     */
    public ExcelArrayReader rowOfNullToEmpty() {
        this.emptyArray = new String[0];
        return this;
    }

    /**
     * 空行返回 长度为length的空数组
     *
     * @param length 长度
     * @return this
     */
    public ExcelArrayReader rowOfNullToEmpty(int length) {
        this.emptyArray = new String[length];
        return this;
    }

    /**
     * 空行返回 emptyArray
     *
     * @param emptyArray 数组
     * @return this
     */
    public ExcelArrayReader rowOfNullToEmpty(String[] emptyArray) {
        this.emptyArray = emptyArray;
        return this;
    }

    /**
     * 设置读取的列
     *
     * @param columns 列
     * @return this
     */
    public ExcelArrayReader columns(int... columns) {
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
    public ExcelArrayReader capacity(int capacity) {
        if (!Arrays1.isEmpty(columns)) {
            throw Exceptions.unsupported("if the column is set the capacity is not supported");
        }
        this.columnSize = capacity;
        return this;
    }

    @Override
    protected String[] parserRow(Row row) {
        if (row == null) {
            return emptyArray;
        }
        if (columnSize == 0) {
            this.columnSize = row.getLastCellNum();
        }
        String[] array = new String[columnSize];
        for (int i = 0; i < columnSize; i++) {
            Cell cell;
            if (Arrays1.isEmpty(columns)) {
                // 读取所有列
                cell = row.getCell(i);
            } else {
                // 读取规定列
                cell = row.getCell(columns[i]);
            }
            // 读取值
            if (cell == null) {
                array[i] = columnEmpty;
            } else {
                String value = Excels.getCellValue(cell);
                if (trim) {
                    array[i] = value.trim();
                } else {
                    array[i] = value;
                }
            }
        }
        return array;
    }

    public int getColumnSize() {
        return columnSize;
    }

}
