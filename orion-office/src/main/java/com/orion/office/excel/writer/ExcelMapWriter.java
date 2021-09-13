package com.orion.office.excel.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * excel map 写入器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/13 14:12
 */
public class ExcelMapWriter<K, V> extends BaseExcelWriter<K, Map<K, V>> {

    public ExcelMapWriter(Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    @Override
    protected Object getValue(Map<K, V> row, K key) {
        return row.get(key);
    }

}
