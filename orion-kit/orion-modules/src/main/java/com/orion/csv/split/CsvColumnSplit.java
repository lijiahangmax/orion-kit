package com.orion.csv.split;

import com.orion.csv.CsvExt;
import com.orion.csv.CsvStream;
import com.orion.csv.core.CsvSymbol;
import com.orion.csv.core.CsvWriter;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * CSV 列拆分器 可以保存数据 只能拆分一个文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 15:15
 */
public class CsvColumnSplit {

    /**
     * 读取流
     */
    private CsvStream stream;

    /**
     * 头部跳过行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] headers;

    /**
     * 列
     */
    private int[] columns;

    /**
     * 列数
     */
    private int length;

    /**
     * 是否流读取 true则不保存数据
     */
    private boolean streaming;

    /**
     * 流式读取缓冲区
     */
    private int bufferLine = 100;

    /**
     * symbol
     */
    private CsvSymbol symbol;

    /**
     * 输出流
     */
    private OutputStream out;

    public CsvColumnSplit(CsvExt ext, int... columns) {
        Valid.notNull(ext, "split ext is null");
        Valid.isFalse(Arrays1.isEmpty(columns), "split columns is null");
        this.stream = ext.stream();
        this.columns = columns;
        this.length = columns.length;
    }

    public CsvColumnSplit(CsvStream stream, int... columns) {
        Valid.notNull(stream, "split stream is null");
        Valid.isFalse(Arrays1.isEmpty(columns), "split columns is null");
        this.stream = stream;
        this.columns = columns;
        this.length = columns.length;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvColumnSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvColumnSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 是否流读取
     *
     * @return this
     */
    public CsvColumnSplit streaming() {
        this.streaming = true;
        return this;
    }

    /**
     * 是否流读取
     *
     * @param bufferLine 流式读取缓冲区
     * @return this
     */
    public CsvColumnSplit streaming(int bufferLine) {
        Valid.lte(0, bufferLine, "bufferLine not be lte 0");
        this.streaming = true;
        this.bufferLine = bufferLine;
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

    /**
     * 设置数据写入的拆分文件
     *
     * @param file file
     * @return this
     */
    public CsvColumnSplit dist(CsvSymbol s, String file) {
        Valid.notNull(file, "write file is null");
        Valid.notNull(s, "csvSymbol is null");
        dist(s, new File(file));
        return this;
    }

    /**
     * 设置数据写入的拆分文件
     *
     * @param file file
     * @return this
     */
    public CsvColumnSplit dist(CsvSymbol s, File file) {
        Valid.notNull(file, "write file is null");
        Valid.notNull(s, "csvSymbol is null");
        Files1.touch(file);
        try {
            this.out = Files1.openOutputStream(file);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        this.symbol = s;
        return this;
    }

    /**
     * 设置数据写入的拆分文件流
     *
     * @param out 流
     * @return this
     */
    public CsvColumnSplit dist(CsvSymbol s, OutputStream out) {
        Valid.notNull(out, "write file is null");
        Valid.notNull(s, "csvSymbol is null");
        this.out = out;
        this.symbol = s;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public CsvColumnSplit execute() {
        Valid.notNull(out, "dist is null");
        stream.skipLines(skip);
        CsvWriter csvWriter = new CsvWriter(out, symbol.getSymbol(), symbol.getCharset());
        try {
            if (headers != null) {
                csvWriter.writeRecord(headers);
            }
            if (streaming) {
                List<String[]> lines;
                while (!Lists.isEmpty(lines = stream.clean().readLines(bufferLine).lines())) {
                    this.write(lines, csvWriter);
                }
            } else {
                this.write(stream.readLines().lines(), csvWriter);
            }
            csvWriter.close();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 写入数据到流
     *
     * @param lines     lines
     * @param csvWriter write
     */
    private void write(List<String[]> lines, CsvWriter csvWriter) throws IOException {
        for (String[] line : lines) {
            String[] c = new String[length];
            for (int i = 0; i < columns.length; i++) {
                int column = this.columns[i];
                if (column >= 0 && column < line.length) {
                    c[i] = line[column];
                }
            }
            csvWriter.writeRecord(c);
        }
    }

    public CsvStream getStream() {
        return stream;
    }

    public List<String[]> lines() {
        return stream.lines();
    }

}
