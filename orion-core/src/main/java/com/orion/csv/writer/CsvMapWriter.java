package com.orion.csv.writer;

import com.orion.utils.Objects1;
import com.orion.utils.Strings;

import java.util.Map;

/**
 * Csv Map导出器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/22 21:55
 */
public class CsvMapWriter<K, V> extends BaseCsvWriter<Map<K, V>, K> {

    public CsvMapWriter(CsvWriter writer) {
        super(writer);
    }

    @Override
    protected String[] parseRow(Map<K, V> row) {
        String[] store;
        if (capacity != -1) {
            store = new String[capacity];
        } else {
            store = new String[maxColumnIndex + 1];
        }
        for (int i = 0; i < store.length; i++) {
            K k = mapping.get(i);
            if (k != null) {
                V v = row.get(k);
                if (v != null) {
                    store[i] = Objects1.toString(v);
                } else {
                    store[i] = defaultValue.getOrDefault(i, Strings.EMPTY);
                }
            } else {
                store[i] = defaultValue.getOrDefault(i, Strings.EMPTY);
            }
        }
        return store;
    }

}
