package com.orion.office.csv.writer;

import com.orion.office.csv.core.CsvWriter;
import com.orion.utils.Objects1;

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
