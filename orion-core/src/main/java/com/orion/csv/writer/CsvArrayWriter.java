package com.orion.csv.writer;

import com.orion.csv.core.CsvWriter;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Csv 数组导出器
 *
 * @author ljh15
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
        } else {
            length = len;
        }
        store = new String[length];
        for (int i = 0; i < length; i++) {
            Integer index = mapping.getOrDefault(i, i);
            if (index >= length) {
                throw Exceptions.index("array length: " + len + " mapping index: " + index);
            }
            if (index >= len) {
                store[i] = defaultValue.getOrDefault(i, Strings.EMPTY);
                continue;
            }
            String s = row[index];
            if (s == null) {
                store[i] = defaultValue.getOrDefault(i, Strings.EMPTY);
            } else {
                store[i] = s;
            }
        }
        return store;
    }

}
