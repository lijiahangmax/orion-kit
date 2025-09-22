/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.office.csv.split;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.office.csv.CsvExt;
import cn.orionsec.kit.office.csv.core.CsvWriter;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import cn.orionsec.kit.office.support.SplitTargetGenerator;

import java.util.Collection;

/**
 * csv 行切分器 可以获取行数据
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/15 10:44
 */
public class CsvRowSplit extends SplitTargetGenerator {

    /**
     * reader
     */
    private final CsvArrayReader reader;

    /**
     * 拆分文件最大行数
     */
    private final int limit;

    /**
     * 表头
     */
    private String[] header;

    /**
     * 列
     */
    private int[] columns;

    private boolean end;

    public CsvRowSplit(CsvExt ext, int limit) {
        this(ext.arrayReader(), limit);
    }

    public CsvRowSplit(CsvArrayReader reader, int limit) {
        Assert.notNull(reader, "reader is null");
        Assert.lte(0, limit, "limit not be lte 0");
        this.reader = reader;
        this.limit = limit;
        this.suffix = Const.SUFFIX_CSV;
    }

    /**
     * 设置后缀
     *
     * @param suffix suffix
     * @return this
     */
    public CsvRowSplit suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvRowSplit skip() {
        return skip(1);
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvRowSplit skip(int skip) {
        reader.skip(skip);
        return this;
    }

    /**
     * 设置读取的列
     *
     * @param columns 列
     * @return this
     */
    public CsvRowSplit columns(int... columns) {
        if (!Arrays1.isEmpty(columns)) {
            this.columns = columns;
        }
        return this;
    }

    /**
     * 设置表头
     *
     * @param header 表头
     * @return ignore
     */
    public CsvRowSplit header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public CsvRowSplit split() {
        do {
            if (!super.hasNext()) {
                this.end = true;
                break;
            }
            // 读取行
            Collection<String[]> rows = reader.clear().read(limit).getRows();
            if (rows.isEmpty()) {
                this.end = true;
                break;
            }
            if (rows.size() < limit) {
                this.end = true;
            }
            super.next();
            CsvArrayWriter currentWriter = new CsvArrayWriter(new CsvWriter(currentOutputStream, reader.getOption().toWriterOption()));
            if (!Arrays1.isEmpty(header)) {
                currentWriter.addRow(header);
            }
            if (Arrays1.isEmpty(columns)) {
                // 读取所有列
                currentWriter.addRows(rows);
            } else {
                // 读取指定列
                for (String[] row : rows) {
                    int length = row.length;
                    String[] newRow = new String[columns.length];
                    for (int i = 0; i < columns.length; i++) {
                        if (length > columns[i]) {
                            newRow[i] = row[columns[i]];
                        }
                    }
                    currentWriter.addRow(newRow);
                }
            }
            currentWriter.flush();
            if (autoClose) {
                currentWriter.close();
            }
        } while (!end);
        return this;
    }

    public CsvArrayReader getReader() {
        return reader;
    }

    public int getLimit() {
        return limit;
    }

}
