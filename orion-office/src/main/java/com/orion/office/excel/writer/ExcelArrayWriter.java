package com.orion.office.excel.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel Array 写入器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/13 13:54
 */
public class ExcelArrayWriter<T> extends BaseExcelWriter<Integer, T[]> {

    public ExcelArrayWriter(Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    @Override
    protected Object getValue(T[] row, Integer key) {
        T value = null;
        if (row.length > key) {
            value = row[key];
        }
        return value;
    }

}
