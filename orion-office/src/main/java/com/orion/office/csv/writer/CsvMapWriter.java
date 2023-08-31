package com.orion.office.csv.writer;

import com.orion.lang.utils.Objects1;
import com.orion.office.csv.core.CsvWriter;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * csv map 导出器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/22 21:55
 */
public class CsvMapWriter<K, V> extends BaseCsvWriter<K, Map<K, V>> {

    public CsvMapWriter(String file) {
        this(new CsvWriter(file));
    }

    public CsvMapWriter(File file) {
        this(new CsvWriter(file));
    }

    public CsvMapWriter(OutputStream out) {
        this(new CsvWriter(out));
    }

    public CsvMapWriter(Writer writer) {
        this(new CsvWriter(writer));
    }

    public CsvMapWriter(CsvWriter writer) {
        super(writer);
    }

    public static <K, V> CsvMapWriter<K, V> create(String file) {
        return new CsvMapWriter<K, V>(file);
    }

    public static <K, V> CsvMapWriter<K, V> create(File file) {
        return new CsvMapWriter<K, V>(file);
    }

    public static <K, V> CsvMapWriter<K, V> create(OutputStream out) {
        return new CsvMapWriter<K, V>(out);
    }

    public static <K, V> CsvMapWriter<K, V> create(Writer writer) {
        return new CsvMapWriter<K, V>(writer);
    }

    public static <K, V> CsvMapWriter<K, V> create(CsvWriter writer) {
        return new CsvMapWriter<K, V>(writer);
    }

    @Override
    protected String[] parseRow(Map<K, V> row) {
        String[] store = super.capacityStore();
        // 设置值
        for (int i = 0; i < store.length; i++) {
            K k = mapping.get(i);
            if (k == null) {
                continue;
            }
            V v = row.get(k);
            if (v != null) {
                store[i] = Objects1.toString(v);
            } else {
                store[i] = Objects1.toString(defaultValue.get(k));
            }
        }
        return store;
    }

}
