package com.orion.office.csv.writer;

import com.orion.lang.utils.Objects1;
import com.orion.office.csv.core.CsvWriter;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

/**
 * csv array 导出器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/22 18:31
 */
public class CsvArrayWriter extends BaseCsvWriter<Integer, String[]> {

    public CsvArrayWriter(String file) {
        this(new CsvWriter(file));
    }

    public CsvArrayWriter(File file) {
        this(new CsvWriter(file));
    }

    public CsvArrayWriter(OutputStream out) {
        this(new CsvWriter(out));
    }

    public CsvArrayWriter(Writer writer) {
        this(new CsvWriter(writer));
    }

    public CsvArrayWriter(CsvWriter writer) {
        super(writer);
    }

    @Override
    protected String[] parseRow(String[] row) {
        String[] store;
        int length, len = row.length;
        if (capacity != -1) {
            length = capacity;
        } else if (maxColumnIndex != 0) {
            length = Math.max(maxColumnIndex + 1, len);
        } else {
            length = len;
        }
        // 指定大小
        store = new String[length];
        for (int i = 0; i < length; i++) {
            Integer index = mapping.getOrDefault(i, i);
            if (index >= length || index >= len) {
                // 默认值
                store[i] = Objects1.toString(defaultValue.get(index));
                continue;
            }
            String s = row[index];
            if (s == null) {
                // 默认值
                store[i] = Objects1.toString(defaultValue.get(index));
            } else {
                store[i] = s;
            }
        }
        return store;
    }

}
