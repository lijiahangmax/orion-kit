/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.office.csv.writer;

import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.office.csv.core.CsvWriter;

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

    public static CsvArrayWriter create(String file) {
        return new CsvArrayWriter(file);
    }

    public static CsvArrayWriter create(File file) {
        return new CsvArrayWriter(file);
    }

    public static CsvArrayWriter create(OutputStream out) {
        return new CsvArrayWriter(out);
    }

    public static CsvArrayWriter create(Writer writer) {
        return new CsvArrayWriter(writer);
    }

    public static CsvArrayWriter create(CsvWriter writer) {
        return new CsvArrayWriter(writer);
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
