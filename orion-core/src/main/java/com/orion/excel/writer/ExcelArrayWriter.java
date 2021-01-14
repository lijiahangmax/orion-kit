package com.orion.excel.writer;

import com.orion.utils.Objects1;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Excel array 写入器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/13 13:54
 */
public class ExcelArrayWriter<T> extends ExcelSheetWriter<T[], Integer> {

    /**
     * 默认值
     */
    private Map<Integer, T> defaultValue = new HashMap<>();

    public ExcelArrayWriter(Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    /**
     * 设置默认值
     *
     * @param column 列
     * @param value  默认值
     * @return this
     */
    public ExcelArrayWriter<T> defaultValue(int column, T value) {
        defaultValue.put(column, value);
        return this;
    }

    @Override
    protected Object getValue(T[] row, Integer key) {
        T value = null;
        if (row.length > key) {
            value = row[key];
        }
        return Objects1.def(value, defaultValue.get(key));
    }

}
