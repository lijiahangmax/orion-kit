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
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV 列拆分器 可以保存数据 可以拆分多个文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 16:14
 */
public class CsvColumnBatchSplit {

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
    private List<String[]> headers;

    /**
     * 列
     */
    private List<int[]> columns;

    /**
     * 拆分文件输出流
     */
    private List<OutputStream> dist;

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

    public CsvColumnBatchSplit(CsvExt ext, List<int[]> columns) {
        Valid.notNull(ext, "split ext is null");
        Valid.notEmpty(columns, "split columns is null");
        for (int[] column : columns) {
            Valid.isFalse(Arrays1.isEmpty(column), "columns is null");
        }
        this.stream = ext.stream();
        this.columns = columns;
    }

    public CsvColumnBatchSplit(CsvStream stream, List<int[]> columns) {
        Valid.notNull(stream, "split stream is null");
        Valid.notEmpty(columns, "split columns is null");
        for (int[] column : columns) {
            Valid.isFalse(Arrays1.isEmpty(column), "columns is null");
        }
        this.stream = stream;
        this.columns = columns;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvColumnBatchSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvColumnBatchSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 每个文件的表头
     * @return ignore
     */
    public CsvColumnBatchSplit header(List<String[]> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 文件的表头
     * @return ignore
     */
    public CsvColumnBatchSplit header(String... headers) {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(headers);
        return this;
    }

    /**
     * 是否流读取
     *
     * @return this
     */
    public CsvColumnBatchSplit streaming() {
        this.streaming = true;
        return this;
    }

    /**
     * 是否流读取
     *
     * @param bufferLine 流式读取缓冲区
     * @return this
     */
    public CsvColumnBatchSplit streaming(int bufferLine) {
        Valid.lte(0, bufferLine, "bufferLine not be lte 0");
        this.streaming = true;
        this.bufferLine = bufferLine;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public CsvColumnBatchSplit execute() {
        Valid.notEmpty(dist, "dist file is empty");
        stream.skipLines(skip);
        try {
            if (streaming) {
                List<String[]> lines;
                Map<Integer, CsvWriter> ws = new HashMap<>();
                for (int i = 0; i < columns.size(); i++) {
                    OutputStream out = dist.get(i);
                    CsvWriter csvWriter = new CsvWriter(out, symbol.getSymbol(), symbol.getCharset());
                    ws.put(i, csvWriter);
                    this.writeHeader(i, csvWriter);
                }
                while (!Lists.isEmpty(lines = stream.clean().readLines(bufferLine).lines())) {
                    for (Map.Entry<Integer, CsvWriter> e : ws.entrySet()) {
                        this.write(lines, e.getValue(), columns.get(e.getKey()));
                    }
                }
                for (Map.Entry<Integer, CsvWriter> e : ws.entrySet()) {
                    e.getValue().close();
                }
            } else {
                stream.readLines();
                for (int i = 0; i < columns.size(); i++) {
                    OutputStream out = dist.get(i);
                    CsvWriter csvWriter = new CsvWriter(out, symbol.getSymbol(), symbol.getCharset());
                    this.writeHeader(i, csvWriter);
                    this.write(stream.lines(), csvWriter, columns.get(i));
                    csvWriter.close();
                }
            }
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
    private void write(List<String[]> lines, CsvWriter csvWriter, int[] columns) throws IOException {
        for (String[] line : lines) {
            String[] c = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                int column = columns[i];
                if (column >= 0 && column < line.length) {
                    c[i] = line[column];
                }
            }
            csvWriter.writeRecord(c);
        }
    }

    /**
     * 写入头到流
     *
     * @param index     头索引
     * @param csvWriter writer
     */
    private void writeHeader(int index, CsvWriter csvWriter) throws IOException {
        int size = Lists.size(headers);
        if (index >= size) {
            return;
        }
        String[] header = headers.get(index);
        if (header != null) {
            csvWriter.writeRecord(header);
        }
    }

    /**
     * 设置拆分文件输出流
     *
     * @param dist dist
     * @return this
     */
    public CsvColumnBatchSplit dist(CsvSymbol s, OutputStream... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        Valid.eq(dist.length, columns.size(), "dist length must eq columns size");
        Valid.notNull(s, "csvSymbol is null");
        this.dist = Lists.of(dist);
        this.symbol = s;
        return this;
    }

    /**
     * 设置拆分文件输出文件
     *
     * @param dist dist
     * @return this
     */
    public CsvColumnBatchSplit dist(CsvSymbol s, File... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        Valid.eq(dist.length, columns.size(), "dist length must eq columns size");
        Valid.notNull(s, "csvSymbol is null");
        List<OutputStream> out = new ArrayList<>();
        for (File file : dist) {
            Files1.touch(file);
            try {
                out.add(Files1.openOutputStream(file));
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        }
        this.dist = out;
        this.symbol = s;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param dist dist
     * @return this
     */
    public CsvColumnBatchSplit dist(CsvSymbol s, String... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        Valid.eq(dist.length, columns.size(), "dist length must eq columns size");
        Valid.notNull(s, "csvSymbol is null");
        List<OutputStream> out = new ArrayList<>();
        for (String file : dist) {
            Files1.touch(file);
            try {
                out.add(Files1.openOutputStream(file));
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        }
        this.dist = out;
        this.symbol = s;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称 不包含后缀
     * @return this
     */
    public CsvColumnBatchSplit distPath(String pathDir, String baseName, CsvSymbol s) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        Valid.notNull(s, "csvSymbol is null");
        List<OutputStream> out = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String path = Files1.getPath(pathDir + "/" + baseName + "_column_split_" + (i + 1) + "." + s.getSuffix());
            Files1.touch(path);
            try {
                out.add(Files1.openOutputStream(path));
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        }
        this.dist = out;
        this.symbol = s;
        return this;
    }

    /**
     * 关闭流
     */
    public void close() {
        for (OutputStream out : dist) {
            Streams.close(out);
        }
    }

    public CsvStream getStream() {
        return stream;
    }

    public List<String[]> lines() {
        return stream.lines();
    }

}
