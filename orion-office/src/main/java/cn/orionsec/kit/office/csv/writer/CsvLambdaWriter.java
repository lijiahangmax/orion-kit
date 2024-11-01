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
import java.util.function.Function;

/**
 * csv lambda 导出器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/8 17:59
 */
public class CsvLambdaWriter<T> extends BaseCsvWriter<Function<T, ?>, T> {

    public CsvLambdaWriter(String file) {
        this(new CsvWriter(file));
    }

    public CsvLambdaWriter(File file) {
        this(new CsvWriter(file));
    }

    public CsvLambdaWriter(OutputStream out) {
        this(new CsvWriter(out));
    }

    public CsvLambdaWriter(Writer writer) {
        this(new CsvWriter(writer));
    }

    public CsvLambdaWriter(CsvWriter writer) {
        super(writer);
    }

    public static <T> CsvLambdaWriter<T> create(String file) {
        return new CsvLambdaWriter<>(file);
    }

    public static <T> CsvLambdaWriter<T> create(File file) {
        return new CsvLambdaWriter<>(file);
    }

    public static <T> CsvLambdaWriter<T> create(OutputStream out) {
        return new CsvLambdaWriter<>(out);
    }

    public static <T> CsvLambdaWriter<T> create(Writer writer) {
        return new CsvLambdaWriter<>(writer);
    }

    public static <T> CsvLambdaWriter<T> create(CsvWriter writer) {
        return new CsvLambdaWriter<>(writer);
    }

    @Override
    protected String[] parseRow(T row) {
        String[] store = super.capacityStore();
        // 设置值
        for (int i = 0; i < store.length; i++) {
            Function<T, ?> fun = mapping.get(i);
            if (fun == null) {
                continue;
            }
            Object v = fun.apply(row);
            if (v != null) {
                store[i] = Objects1.toString(v);
            } else {
                store[i] = Objects1.toString(defaultValue.get(fun));
            }
        }
        return store;
    }

}
