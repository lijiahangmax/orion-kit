package com.orion.excel.reader;

import com.orion.excel.Excels;
import com.orion.utils.Arrays1;
import com.orion.utils.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel 文本读取器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/5 10:04
 */
public class ExcelArrayReader extends ExcelReader<String[]> {

    /**
     * 空列
     */
    private String cellEmpty;

    /**
     * 空行
     */
    private String[] emptyArray;

    public ExcelArrayReader(Workbook workbook, Sheet sheet) {
        super(workbook, sheet, new ArrayList<>(), null);
    }

    public ExcelArrayReader(Workbook workbook, Sheet sheet, List<String[]> rows) {
        super(workbook, sheet, rows, null);
    }

    public ExcelArrayReader(Workbook workbook, Sheet sheet, Consumer<String[]> consumer) {
        super(workbook, sheet, null, consumer);
    }

    /**
     * 设置单元格为空的值
     *
     * @param text text
     * @return this
     */
    public ExcelArrayReader cellOfNull(String text) {
        this.cellEmpty = text;
        return this;
    }

    /**
     * 设置单元格为空的值为 ""
     *
     * @return this
     * @see Strings#EMPTY
     */
    public ExcelArrayReader cellOfNullToEmpty() {
        this.cellEmpty = Strings.EMPTY;
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

    @Override
    protected String[] parserRow(Row row) {
        if (row == null) {
            return emptyArray;
        }
        if (columnSize == 0) {
            columnSize = row.getLastCellNum();
        }
        String[] array = new String[columnSize];
        for (int i = 0; i < columnSize; i++) {
            Cell cell;
            if (Arrays1.isEmpty(columns)) {
                cell = row.getCell(i);
            } else {
                cell = row.getCell(columns[i]);
            }
            if (cell == null) {
                array[i] = cellEmpty;
            } else {
                array[i] = Excels.getCellValue(cell);
            }
        }
        return array;
    }

}
