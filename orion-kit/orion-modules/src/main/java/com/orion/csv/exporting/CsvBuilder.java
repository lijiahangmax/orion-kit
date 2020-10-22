package com.orion.csv.exporting;

import com.orion.able.BuilderAble;
import com.orion.csv.core.CsvWriter;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.reflect.Methods;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 构建器 会保存数据 写操作最终执行
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/2 0:57
 */
public class CsvBuilder implements BuilderAble<CsvBuilder> {

    /**
     * 头
     */
    private List<String[]> headers = new ArrayList<>();

    /**
     * 行
     */
    private List<String[]> records = new ArrayList<>();

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
    private int columnCount;

    /**
     * out
     */
    private OutputStream out;

    /**
     * writer
     */
    private Writer writer;

    public CsvBuilder() {
    }

    public CsvBuilder(String[] headers) {
        this.headers.add(headers);
    }

    public CsvBuilder(List<String[]> records) {
        if (!Lists.isEmpty(records)) {
            String[] record = records.get(0);
            if (record != null) {
                this.columnCount = record.length;
            }
        }
        this.records.addAll(records);
    }

    public CsvBuilder(String[] headers, List<String[]> records) {
        if (!Lists.isEmpty(records)) {
            String[] record = records.get(0);
            if (record != null) {
                this.columnCount = record.length;
            }
        }
        this.headers.add(headers);
        this.records.addAll(records);
    }

    public <T> CsvBuilder(List<T> records, String[] fields) {
        toStrings(records, fields);
    }

    public <T> CsvBuilder(String[] headers, List<T> records, String[] fields) {
        this.headers.add(headers);
        toStrings(records, fields);
    }

    /**
     * 设置列数 用于空行
     *
     * @param columnCount 列数
     * @return this
     */
    public CsvBuilder columnCount(int columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    /**
     * 设置编码格式
     *
     * @param charset charset
     * @return this
     */
    public CsvBuilder charset(String charset) {
        this.charset = Charset.forName(charset);
        return this;
    }

    /**
     * 设置编码格式
     *
     * @param charset charset
     * @return this
     */
    public CsvBuilder charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 设置分隔符
     *
     * @param symbol 分隔符
     * @return this
     */
    public CsvBuilder symbol(char symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * 添加头
     *
     * @param headers 头
     * @return this
     */
    public CsvBuilder addHeaders(String[] headers) {
        this.headers.add(headers);
        return this;
    }

    /**
     * 添加多个头
     *
     * @param headers 头
     * @return this
     */
    public CsvBuilder addHeaders(List<String[]> headers) {
        this.headers.addAll(headers);
        return this;
    }

    /**
     * 添加记录
     *
     * @param records 记录
     * @return this
     */
    public CsvBuilder addRecord(String[] records) {
        if (this.columnCount != 0) {
            this.columnCount = records.length;
        }
        this.records.add(records);
        return this;
    }

    /**
     * 添加多条记录
     *
     * @param records 记录
     * @return this
     */
    public CsvBuilder addRecords(List<String[]> records) {
        if (!Lists.isEmpty(records)) {
            String[] record = records.get(0);
            if (record != null) {
                if (this.columnCount != 0) {
                    this.columnCount = record.length;
                }
            }
        }
        this.records.addAll(records);
        return this;
    }

    /**
     * 添加记录
     *
     * @param records 记录
     * @param fields  字段
     * @return this
     */
    @SuppressWarnings("unchecked")
    public <T> CsvBuilder addRecord(T records, String[] fields) {
        toStrings(Lists.of(records), fields);
        return this;
    }

    /**
     * 添加多条记录
     *
     * @param records 记录
     * @param fields  字段
     * @return this
     */
    public <T> CsvBuilder addRecords(List<T> records, String[] fields) {
        toStrings(records, fields);
        return this;
    }

    /**
     * 设置目标文件
     *
     * @param file 文件
     * @return this
     */
    public CsvBuilder dist(File file) {
        Valid.notNull(file, "file is null");
        Files1.touch(file);
        out = Files1.openOutputStreamSafe(file);
        return this;
    }

    /**
     * 设置目标文件
     *
     * @param file 文件
     * @return this
     */
    public CsvBuilder dist(String file) {
        Valid.notNull(file, "file is null");
        return dist(new File(file));
    }

    /**
     * 设置目标流
     *
     * @param out out
     * @return this
     */
    public CsvBuilder dist(OutputStream out) {
        this.out = out;
        return this;
    }

    /**
     * 设置目标流
     *
     * @param writer writer
     * @return this
     */
    public CsvBuilder dist(Writer writer) {
        this.writer = writer;
        return this;
    }

    /**
     * 创建一行空行
     *
     * @return this
     */
    public CsvBuilder newLine() {
        if (this.columnCount == 0) {
            if (!Lists.isEmpty(headers)) {
                String[] header = headers.get(0);
                if (header != null) {
                    this.columnCount = header.length;
                }
            }
        }
        if (this.columnCount == 0) {
            this.columnCount = 1;
        }
        if (Lists.isEmpty(records)) {
            this.headers.add(new String[columnCount]);
        } else {
            this.records.add(new String[columnCount]);
        }
        return this;
    }

    @Override
    public CsvBuilder build() {
        if (out == null && writer == null) {
            throw Exceptions.argument("out or writer is null");
        }
        try {
            CsvWriter csvWriter;
            if (out != null) {
                csvWriter = new CsvWriter(out, symbol, charset);
            } else {
                csvWriter = new CsvWriter(writer, symbol);
            }
            for (String[] header : headers) {
                csvWriter.writeRecord(header);
            }
            for (String[] record : records) {
                csvWriter.writeRecord(record);
            }
            csvWriter.flush();
            Streams.close(csvWriter);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 关闭流
     *
     * @return this
     */
    public CsvBuilder close() {
        Streams.close(out);
        Streams.close(writer);
        return this;
    }

    /**
     * bean to string[], 调用字段的getter方法
     *
     * @param records beans
     * @param fields  字段
     * @param <T>     T
     */
    private <T> void toStrings(List<T> records, String[] fields) {
        this.columnCount = fields.length;
        for (T record : records) {
            String[] r = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                r[i] = Methods.invokeGetter(record, fields[i]);
            }
            this.records.add(r);
        }
    }

    public List<String[]> getHeaders() {
        return headers;
    }

    public List<String[]> getRecords() {
        return records;
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
