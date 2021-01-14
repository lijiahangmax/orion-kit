package com.orion.excel.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Excel map 写入器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/13 14:12
 */
public class ExcelMapWriter<K, V> extends ExcelSheetWriter<Map<K, V>, K> {

    /**
     * 默认值
     */
    private Map<K, V> defaultValue = new HashMap<>();

    public ExcelMapWriter(Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    /**
     * 默认值
     *
     * @param k key
     * @param v value
     * @return this
     */
    public ExcelMapWriter<K, V> defaultValue(K k, V v) {
        this.defaultValue.put(k, v);
        return this;
    }

    @Override
    protected Object getValue(Map<K, V> row, K key) {
        return row.getOrDefault(key, defaultValue.get(key));
    }

}
