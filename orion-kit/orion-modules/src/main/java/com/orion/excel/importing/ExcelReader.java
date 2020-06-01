package com.orion.excel.importing;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.excel.Excels;
import com.orion.utils.Valid;
import com.orion.utils.reflect.BeanWrapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel 读取器 不支持流式读取 支持随机读写 不支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 15:41
 */
public class ExcelReader {

    private Sheet sheet;

    /**
     * 列数
     */
    private int columnNum;

    /**
     * 行数
     */
    private int rowNum;

    /**
     * 首行
     */
    private Row firstRow;

    /**
     * 行索引
     */
    private int rowIndex;

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

    public ExcelReader(Sheet sheet) {
        this(sheet, null);
    }

    public ExcelReader(Sheet sheet, int[] columns) {
        Valid.notNull(sheet, "Sheet is null");
        this.sheet = sheet;
        this.columns = columns;
        this.rowNum = sheet.getLastRowNum() + 1;
        this.firstRow = sheet.getRow(0);
        if (this.firstRow != null) {
            this.columnNum = this.firstRow.getLastCellNum();
        }
    }

    // --------------- skip ---------------

    /**
     * 跳过行
     *
     * @return this
     */
    public ExcelReader skipRow() {
        rowIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param row 行
     * @return this
     */
    public ExcelReader skipRows(int row) {
        rowIndex += row;
        return this;
    }

    /**
     * 是否跳过空行
     *
     * @param skip 如果row为null true: continue false: rows.add(null), row = null
     * @return this
     */
    public ExcelReader skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    // --------------- read ---------------

    /**
     * 读取一行
     *
     * @return this
     */
    public ExcelReader readRow() {
        readOneRow();
        return this;
    }

    /**
     * 读取多行
     *
     * @param line 行
     * @return this
     */
    public ExcelReader readRows(int line) {
        for (int i = 0; i < line && rowIndex < rowNum; i++) {
            readOneRow();
        }
        return this;
    }

    /**
     * 读取剩余行
     *
     * @return this
     */
    public ExcelReader readRows() {
        while (rowIndex < rowNum) {
            readOneRow();
        }
        return this;
    }

    /**
     * 读取一行
     */
    private void readOneRow() {
        Row row = sheet.getRow(rowIndex++);
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
                rowString[i] = Excels.getValue(row.getCell(columns[i]));
            }
        } else {
            int rowCellNum = row.getLastCellNum();
            rowString = new String[rowCellNum];
            for (int i = 0; i < rowCellNum; i++) {
                rowString[i] = Excels.getValue(row.getCell(i));
            }
        }
        return rowString;
    }

    // --------------- result ---------------

    /**
     * 读取行 不添加到读取记录 可能会返回null
     * 不支持流式读取
     *
     * @param i 行索引
     * @return 行
     */
    public String[] readRowRecord(int i) {
        return parseRow(sheet.getRow(i));
    }

    /**
     * 读取多行 不添加到读取记录 如果skipNullRows为true, list不可能包含null
     * 不支持流式读取
     *
     * @param start 开始行
     * @param end   结束行
     * @return 行
     */
    public List<String[]> readRowsRecord(int start, int end) {
        List<String[]> list = new ArrayList<>();
        for (int i = start; i < end && i < rowNum; i++) {
            String[] row = parseRow(sheet.getRow(i));
            if (row == null) {
                if (!this.skipNullRows) {
                    list.add(null);
                }
            } else {
                list.add(row);
            }
        }
        return list;
    }

    /**
     * 读取多行 不添加到读取记录 如果skipNullRows为true, list不可能包含null
     * 不支持流式读取
     *
     * @param start 开始行
     * @return 行
     */
    public List<String[]> readRowsRecord(int start) {
        List<String[]> list = new ArrayList<>();
        while (start < rowNum) {
            String[] row = parseRow(sheet.getRow(start++));
            if (row == null) {
                if (!this.skipNullRows) {
                    list.add(null);
                }
            } else {
                list.add(row);
            }
        }
        return list;
    }

    /**
     * 以某一行为基准 设置列数
     *
     * @param rowIndex 行索引
     * @return 列数
     */
    public int setColumnNum(int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            columnNum = row.getLastCellNum();
        }
        return columnNum;
    }

    /**
     * 获取当前sheet
     *
     * @return sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * 获取列数
     *
     * @return 列数
     */
    public int getColumnNum() {
        return columnNum;
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
     * 获取首行
     * 流读取则为null
     *
     * @return 首行
     */
    public Row getFirstRow() {
        return firstRow;
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
     * 获取读取的所有行 如果skipNullRows为true, list不可能包含null
     *
     * @return 行
     */
    public List<String[]> rows() {
        return rows;
    }

    /**
     * 判断是否为流式读取
     *
     * @return true流式读取
     */
    public boolean isStreaming() {
        return sheet instanceof StreamingSheet;
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
