package com.orion.office.csv.split;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.office.csv.CsvExt;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.csv.writer.CsvArrayWriter;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;

/**
 * csv 拆分一个文件 占用内存少
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/15 15:15
 */
public class CsvColumnSplit implements SafeCloseable {

    /**
     * 读取流
     */
    private final CsvArrayReader reader;

    /**
     * 列
     */
    private final int[] columns;

    /**
     * 表头
     */
    private String[] headers;

    /**
     * 流式读取缓冲区
     */
    private int bufferLine;

    public CsvColumnSplit(CsvExt ext, int... columns) {
        this(ext.arrayReader(), columns);
    }

    public CsvColumnSplit(CsvArrayReader reader, int... columns) {
        Valid.notNull(reader, "split reader is null");
        Valid.isFalse(Arrays1.isEmpty(columns), "split columns is null");
        this.reader = reader;
        this.columns = columns;
        this.bufferLine = Const.N_100;
    }

    /**
     * 设置 读取行缓冲区
     *
     * @param bufferLine 缓冲区行数
     * @return this
     */
    public CsvColumnSplit bufferLine(int bufferLine) {
        this.bufferLine = bufferLine;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvColumnSplit skip() {
        reader.skip();
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvColumnSplit skip(int skip) {
        reader.skip(skip);
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 文件的表头
     * @return ignore
     */
    public CsvColumnSplit header(String... headers) {
        this.headers = headers;
        return this;
    }

    public CsvColumnSplit split(File file) {
        return this.split(Files1.openOutputStreamSafe(file), false);
    }

    public CsvColumnSplit split(String file) {
        return this.split(Files1.openOutputStreamSafe(file), false);
    }

    public CsvColumnSplit split(OutputStream out) {
        return this.split(out, false);
    }

    /**
     * 执行拆分
     *
     * @param out   out
     * @param close 是否关闭流
     * @return this
     */
    public CsvColumnSplit split(OutputStream out, boolean close) {
        CsvArrayWriter writer = new CsvArrayWriter(out);
        int length = columns.length;
        writer.capacity(length);
        // 设置表头
        if (!Arrays1.isEmpty(headers)) {
            writer.headers(headers);
        }
        Collection<String[]> rows;
        // 读取行
        while (!Lists.isEmpty(rows = reader.clear().read(bufferLine).getRows())) {
            for (String[] row : rows) {
                int rowLength = row.length;
                String[] newRow = new String[length];
                for (int i = 0; i < length; i++) {
                    if (rowLength > columns[i]) {
                        newRow[i] = row[columns[i]];
                    }
                }
                writer.addRow(newRow);
            }
        }
        writer.flush();
        if (close) {
            writer.close();
        }
        return this;
    }

    @Override
    public void close() {
        reader.close();
    }

    public CsvArrayReader getReader() {
        return reader;
    }

    public int[] getColumns() {
        return columns;
    }

}
