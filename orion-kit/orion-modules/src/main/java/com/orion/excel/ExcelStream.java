package com.orion.excel;

import com.orion.utils.Valid;
import com.orion.utils.reflect.BeanWrapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 15:41
 */
public class ExcelStream {

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
     * 读取的行
     */
    private String[] row;

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
        return readRow(true);
    }

    /**
     * 读取多行
     *
     * @param line 行
     * @return this
     */
    public ExcelStream readRows(int line) {
        for (int i = 0; i < line && rowIndex < rowNum; i++) {
            readRow(false);
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
            readRow(false);
        }
        return this;
    }

    /**
     * 读取一行
     *
     * @param setRow 是否设置row
     * @return this
     */
    private ExcelStream readRow(boolean setRow) {
        Row row = sheet.getRow(rowIndex++);
        String[] rowString = readRow(row);
        if (setRow) {
            this.row = rowString;
        }
        if (rowString == null) {
            if (!skipNullRows) {
                rows.add(null);
            }
        } else {
            rows.add(rowString);
        }
        return this;
    }

    /**
     * 读取row
     *
     * @param row row
     * @return String[]
     */
    private String[] readRow(Row row) {
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
     *
     * @param i 行索引
     * @return 行
     */
    public String[] readRowRecord(int i) {
        return readRow(sheet.getRow(i));
    }

    /**
     * 读取多行 不添加到读取记录 如果skipNullRows为true, list不可能包含null
     *
     * @param start 开始行
     * @param end   结束行
     * @return 行
     */
    public List<String[]> readRowsRecord(int start, int end) {
        List<String[]> list = new ArrayList<>();
        for (int i = start; i < end && i < rowNum; i++) {
            String[] row = readRow(sheet.getRow(i));
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
     *
     * @param start 开始行
     * @return 行
     */
    public List<String[]> readRowsRecord(int start) {
        List<String[]> list = new ArrayList<>();
        while (start < rowNum) {
            String[] row = readRow(sheet.getRow(start++));
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
     * 设置列数
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
     *
     * @return 首行
     */
    public Row getFirstRow() {
        return firstRow;
    }

    /**
     * 获取读取的行 可能为null
     *
     * @return 行
     */
    public String[] row() {
        return row;
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
     * 单行转bean 可能为null
     *
     * @param clazz beanClass
     * @param map   fieldMap
     * @param <T>   T
     * @return bean
     */
    public <T> T toBean(Class<T> clazz, Map<Integer, String> map) {
        if (row == null) {
            return null;
        }
        return BeanWrapper.toBean(row, map, clazz);
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
     * 单行转Map 可能为null
     *
     * @param map fieldMap
     * @return bean
     */
    public Map<String, String> toMap(Map<Integer, String> map) {
        if (row == null) {
            return null;
        }
        return BeanWrapper.toMap(row, map);
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
