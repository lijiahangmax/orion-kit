package com.orion.csv.exporting;

import com.orion.csv.core.CsvSymbol;
import com.orion.csv.core.CsvWriter;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * CSV 构建器 不会保存数据 写操作立即执行
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/18 0:44
 */
public class CsvExport {

    /**
     * 分割字符
     */
    private char symbol = ',';

    /**
     * 编码格式
     */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * 列数
     */
    private int columnCount = 1;

    /**
     * out
     */
    private OutputStream out;

    /**
     * writer
     */
    private Writer writer;

    /**
     * csvWriter
     */
    private CsvWriter csvWriter;

    public CsvExport(String file) {
        this(new File(file), null);
    }

    public CsvExport(String file, CsvSymbol symbol) {
        this(new File(file), symbol);
    }

    public CsvExport(File file) {
        this(file, null);
    }

    public CsvExport(File file, CsvSymbol symbol) {
        if (symbol != null) {
            this.symbol = symbol.getSymbol();
            this.charset = symbol.getCharset();
        }
        this.out = Files1.openOutputStreamSafe(file);
        this.csvWriter = new CsvWriter(out, this.symbol, this.charset);
    }

    public CsvExport(OutputStream out) {
        this(out, null);
    }

    public CsvExport(OutputStream out, CsvSymbol symbol) {
        this.out = out;
        if (symbol != null) {
            this.symbol = symbol.getSymbol();
            this.charset = symbol.getCharset();
        }
        this.csvWriter = new CsvWriter(out, this.symbol, this.charset);
    }

    public CsvExport(Writer writer) {
        this(writer, null);
    }

    public CsvExport(Writer writer, CsvSymbol symbol) {
        this.writer = writer;
        if (symbol != null) {
            this.symbol = symbol.getSymbol();
        }
        this.csvWriter = new CsvWriter(writer, this.symbol);
    }

    /**
     * 设置列数 用于空行
     *
     * @param columnCount 列数
     * @return this
     */
    public CsvExport columnCount(int columnCount) {
        Valid.gt(columnCount, 0, "column count must greater than 0");
        this.columnCount = columnCount;
        return this;
    }

    /**
     * 创建一行空行
     *
     * @return this
     */
    public CsvExport newLine() {
        return newLines(1);
    }

    /**
     * 创建多行空行
     *
     * @param i 行数
     * @return this
     */
    public CsvExport newLines(int i) {
        for (int i1 = 0; i1 < i; i1++) {
            addRecord(new String[columnCount]);
        }
        return this;
    }

    /**
     * 添加一行
     *
     * @param row row
     * @return this
     */
    public CsvExport addRecord(String[] row) {
        try {
            csvWriter.writeRecord(row);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 添加多行
     *
     * @param rows rows
     * @return this
     */
    public CsvExport addRecords(List<String[]> rows) {
        for (String[] row : rows) {
            this.addRecord(row);
        }
        return this;
    }

    public CsvExport flush() {
        csvWriter.flush();
        return this;
    }

    /**
     * 关闭流
     *
     * @return this
     */
    public CsvExport close() {
        Streams.close(out);
        Streams.close(writer);
        Streams.close(csvWriter);
        return this;
    }

    public char getSymbol() {
        return symbol;
    }

    public Charset getCharset() {
        return charset;
    }

    public int getColumnCount() {
        return columnCount;
    }

}
