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
package com.orion.office.csv.merge;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Streams;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.csv.writer.CsvArrayWriter;

import java.util.Collection;

/**
 * csv 行合并器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/15 17:10
 */
public class CsvMerge implements SafeCloseable {

    /**
     * 输出流
     */
    private final CsvArrayWriter writer;

    /**
     * 跳过合并文件的行
     */
    private int skipRows;

    /**
     * 读取行缓冲区
     */
    private int bufferLine;

    public CsvMerge(CsvArrayWriter writer) {
        this.writer = writer;
        this.bufferLine = Const.N_100;
    }

    /**
     * 设置文件头
     *
     * @param header header
     * @return this
     */
    public CsvMerge header(String... header) {
        this.writer.headers(header);
        return this;
    }

    /**
     * 设置 读取行缓冲区
     *
     * @param bufferLine 缓冲区行数
     * @return this
     */
    public CsvMerge bufferLine(int bufferLine) {
        this.bufferLine = bufferLine;
        return this;
    }

    /**
     * 合并csv跳过一行
     *
     * @return this
     */
    public CsvMerge skip() {
        writer.skip();
        return this;
    }

    /**
     * 合并csv跳过多行
     *
     * @param skip 文件跳过的行
     * @return this
     */
    public CsvMerge skip(int skip) {
        writer.skip(skip);
        return this;
    }

    /**
     * 跳过合并文件一行
     *
     * @return this
     */
    public CsvMerge skipRows() {
        skipRows += 1;
        return this;
    }

    /**
     * 跳过合并文件多行
     *
     * @param skip skip
     * @return this
     */
    public CsvMerge skipRows(int skip) {
        skipRows += skip;
        return this;
    }

    /**
     * 执行合并
     *
     * @return this
     */
    public CsvMerge merge(CsvArrayReader reader) {
        if (skipRows != 0) {
            reader.skip(skipRows);
        }
        // 数据
        Collection<String[]> lines;
        while (!(lines = reader.clear().read(bufferLine).getRows()).isEmpty()) {
            writer.addRows(lines);
        }
        this.skipRows = 0;
        writer.flush();
        return this;
    }

    @Override
    public void close() {
        Streams.close(writer);
    }

    public CsvArrayWriter getWriter() {
        return writer;
    }

}
