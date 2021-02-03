package com.orion.excel.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * Excel Map 写入器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/13 14:12
 */
public class ExcelMapWriter<K, V> extends BaseExcelSheetWriter<K, Map<K, V>> {

    public ExcelMapWriter(Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    @Override
    protected Object getValue(Map<K, V> row, K key) {
        return row.get(key);
    }

}
