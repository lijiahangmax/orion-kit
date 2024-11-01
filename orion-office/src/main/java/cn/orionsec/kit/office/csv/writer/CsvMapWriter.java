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
