package com.orion;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.excel.Excels;
import com.orion.utils.Valid;
import com.orion.utils.reflect.BeanWrapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Excel 流式读取器 不支持随机读写 不支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 15:41
 */
public class ExcelStream {

    private Sheet sheet;

    /**
     * 行数
     */
    private int rowNum;

    /**
     * 包含跳过的索引位
     */
    private int rowIndex;

    /**
     * 流迭代器索引
     */
    private int iterableIndex;

    /**
     * 流迭代器索引
     */
    private Iterator<Row> streamIterable;

    /**
     * 读取的列
     */
    private int[] columns;

    /**
     * 是否跳过空行
     */
    private boolean skipNullRows = true;

    /**
     * 读取的所有行
     */
    private List<String[]> rows = new ArrayList<>();

    public ExcelStream(Sheet sheet) {
        this(sheet, null);
    }

    public ExcelStream(Sheet sheet, int[] columns) {
        Valid.notNull(sheet, "Sheet is null");
        this.sheet = sheet;
        this.columns = columns;
        this.rowNum = sheet.getLastRowNum() + 1;
        this.streamIterable = sheet.iterator();
    }

    // --------------- skip ---------------

    /**
     * 跳过行
     *
     * @return this
     */
    public ExcelStream skipRow() {
        rowIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param row 行
     * @return this
     */
    public ExcelStream skipRows(int row) {
        rowIndex += row;
        return this;
    }

    /**
     * 是否跳过空行
     *
     * @param skip 如果row为null true: continue false: rows.add(null), row = null
     * @return this
     */
    public ExcelStream skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    // --------------- read ---------------

    /**
     * 读取一行
     *
     * @return this
     */
    public ExcelStream readRow() {
        readOneRow();
        return this;
    }

    /**
     * 读取多行 需要包含跳过的行
     *
     * @param line 行
     * @return this
     */
    public ExcelStream readRows(int line) {
        if (iterableIndex != rowIndex) {
            line += rowIndex - iterableIndex;
        }
        for (int i = 0; i < line && iterableIndex < rowNum; i++) {
            readOneRow();
        }
        return this;
    }

    /**
     * 读取剩余行
     *
     * @return this
     */
    public ExcelStream readRows() {
        while (rowIndex < rowNum) {
            readOneRow();
        }
        return this;
    }

    /**
     * 读取一行
     */
    private void readOneRow() {
        Row row;
        if (streamIterable.hasNext()) {
            row = streamIterable.next();
            if (iterableIndex++ < rowIndex) {
                return;
            }
        } else {
            return;
        }
        rowIndex++;
        String[] rowString = parseRow(row);
        if (rowString == null) {
            if (!skipNullRows) {
                rows.add(null);
            }
        } else {
            rows.add(rowString);
        }
    }

    /**
     * 解析row
     *
     * @param row row
     * @return String[]
     */
    private String[] parseRow(Row row) {
        if (row == null) {
            return null;
        }
        String[] rowString;
        if (columns != null) {
            rowString = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                rowString[i] = Excels.getCellValue(row.getCell(columns[i]));
            }
        } else {
            int rowCellNum = row.getLastCellNum();
            rowString = new String[rowCellNum];
            for (int i = 0; i < rowCellNum; i++) {
                rowString[i] = Excels.getCellValue(row.getCell(i));
            }
        }
        return rowString;
    }

    // --------------- result ---------------

    /**
     * 获取当前sheet
     *
     * @return sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * 获取当前行索引
     *
     * @return 当前行索引
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * 获取读取的列的索引
     *
     * @return 读取的列的索引
     */
    public int[] getColumns() {
        return columns;
    }

    /**
     * 获取行数
     *
     * @return 行数
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * 是否跳过空row
     *
     * @return true跳过
     */
    public boolean isSkipNullRows() {
        return skipNullRows;
    }

    /**
     * 是否为流式读取
     *
     * @return true 流式读取
     */
    public boolean isStreaming() {
        return sheet instanceof StreamingSheet;
    }

    /**
     * 获取读取的所有行 如果skipNullRows为true, list不可能包含null
     *
     * @return 行
     */
    public List<String[]> rows() {
        return rows;
    }

    /**
     * 多行转bean
     *
     * @param clazz beanClass
     * @param map   fieldMap
     * @param <T>   T
     * @return bean
     */
    public <T> List<T> toBeans(Class<T> clazz, Map<Integer, String> map) {
        List<T> list = new ArrayList<>();
        for (String[] row : rows) {
            if (row == null) {
                if (!this.skipNullRows) {
                    list.add(null);
                }
            } else {
                list.add(BeanWrapper.toBean(row, map, clazz));
            }
        }
        return list;
    }

    /**
     * 多行转Map 如果skipNullRows为true, list不可能包含null
     *
     * @param map fieldMap
     * @return bean
     */
    public List<Map<String, String>> toMaps(Map<Integer, String> map) {
        List<Map<String, String>> list = new ArrayList<>();
        for (String[] row : rows) {
            if (row == null) {
                if (!this.skipNullRows) {
                    list.add(null);
                }
            } else {
                list.add(BeanWrapper.toMap(row, map));
            }
        }
        return list;
    }

}
