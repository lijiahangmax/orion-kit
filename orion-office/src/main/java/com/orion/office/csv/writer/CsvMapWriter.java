package com.orion.office.csv.writer;

import com.orion.office.csv.core.CsvWriter;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * Csv Map 导出器
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
            if (k == null) {
                continue;
            }
            V v = row.get(k);
            if (v != null) {
                store[i] = Objects1.toString(v);
            } else {
                store[i] = defaultValue.getOrDefault(k, Strings.EMPTY);
            }
        }
        return store;
    }

}
