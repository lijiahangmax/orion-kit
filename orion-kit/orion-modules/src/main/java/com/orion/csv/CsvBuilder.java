package com.orion.csv;

import com.orion.able.Builderable;
import com.orion.csv.core.CsvWriter;
import com.orion.utils.Exceptions;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.reflect.Methods;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 构建器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/2 0:57
 */
public class CsvBuilder implements Builderable<CsvBuilder> {

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
    private char splitChar = ',';

    /**
     * 编码格式
     */
    private Charset charset = Charset.forName("UTF-8");

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
     * 设置分隔符
     *
     * @param splitChar 分隔符
     * @return this
     */
    public CsvBuilder splitChar(char splitChar) {
        this.splitChar = splitChar;
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
        this.columnCount = records.length;
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
                this.columnCount = record.length;
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
     * 写入到文件
     *
     * @param file 文件
     * @return this
     * @throws IOException IOException
     */
    public CsvBuilder write(File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            Files1.touch(file);
        }
        out = Files1.openOutputStream(file);
        return this;
    }

    /**
     * 写入到文件
     *
     * @param file 文件
     * @return this
     * @throws IOException IOException
     */
    public CsvBuilder write(String file) throws IOException {
        return write(new File(file));
    }

    /**
     * 写入到流
     *
     * @param out out
     * @return this
     */
    public CsvBuilder write(OutputStream out) {
        this.out = out;
        return this;
    }

    /**
     * 写入到流
     *
     * @param writer writer
     * @return this
     */
    public CsvBuilder write(Writer writer) {
        this.writer = writer;
        return this;
    }

    /**
     * 创建一空行
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
                csvWriter = new CsvWriter(out, splitChar, charset);
            } else {
                csvWriter = new CsvWriter(writer, splitChar);
            }
            for (String[] header : headers) {
                csvWriter.writeRecord(header);
            }
            for (String[] record : records) {
                csvWriter.writeRecord(record);
            }
            csvWriter.close();
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

    public char getSplitChar() {
        return splitChar;
    }

    public Charset getCharset() {
        return charset;
    }

    public int getColumnCount() {
        return columnCount;
    }

}
