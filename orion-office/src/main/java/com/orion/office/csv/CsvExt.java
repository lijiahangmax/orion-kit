package com.orion.office.csv;

import com.orion.able.SafeCloseable;
import com.orion.lang.collect.MutableMap;
import com.orion.office.csv.core.CsvReader;
import com.orion.office.csv.option.CsvReaderOption;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.csv.reader.CsvBeanReader;
import com.orion.office.csv.reader.CsvMapReader;
import com.orion.office.csv.reader.CsvRawReader;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.function.Consumer;

/**
 * CSV 提取器
 * 也支持其他格式的文本文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/27 11:07
 */
public class CsvExt implements SafeCloseable {

    private CsvReader reader;

    public CsvExt(File file) {
        this(Files1.openInputStreamSafe(file), null);
    }

    public CsvExt(File file, CsvReaderOption option) {
        this(Files1.openInputStreamSafe(file), option);
    }

    public CsvExt(String file) {
        this(Files1.openInputStreamSafe(file), null);
    }

    public CsvExt(String file, CsvReaderOption option) {
        this(Files1.openInputStreamSafe(file), option);
    }

    public CsvExt(InputStream in) {
        this(in, null);
    }

    public CsvExt(InputStream in, CsvReaderOption option) {
        this(new CsvReader(in, option));
    }

    public CsvExt(Reader reader) {
        this(reader, null);
    }

    public CsvExt(Reader reader, CsvReaderOption option) {
        this(new CsvReader(reader, option));
    }

    public CsvExt(CsvReader reader) {
        this.reader = reader;
    }

    /**
     * 将文本转化为 CsvExt
     *
     * @param s s
     * @return CsvExt
     */
    public static CsvExt parse(String s) {
        return new CsvExt(CsvReader.parse(s));
    }

    /**
     * 将文本转化为 CsvExt
     *
     * @param s      s
     * @param option 配置项
     * @return CsvExt
     */
    public static CsvExt parse(String s, CsvReaderOption option) {
        return new CsvExt(CsvReader.parse(s, option));
    }

    // -------------------- array reader --------------------

    public CsvArrayReader arrayReader() {
        return new CsvArrayReader(reader);
    }

    public CsvArrayReader arrayReader(List<String[]> rows) {
        return new CsvArrayReader(reader, rows);
    }

    /**
     * 获取array读取器
     *
     * @param consumer consumer
     * @return CsvArrayReader
     */
    public CsvArrayReader arrayReader(Consumer<String[]> consumer) {
        return new CsvArrayReader(reader, consumer);
    }

    // -------------------- map reader --------------------

    public <K, V> CsvMapReader<K, V> mapReader() {
        return new CsvMapReader<>(reader);
    }

    public <K, V> CsvMapReader<K, V> mapReader(List<MutableMap<K, V>> rows) {
        return new CsvMapReader<>(reader, rows);
    }

    /**
     * 获取map读取器
     *
     * @param consumer consumer
     * @param <K>      K
     * @param <V>      V
     * @return CsvMapReader
     */
    public <K, V> CsvMapReader<K, V> mapReader(Consumer<MutableMap<K, V>> consumer) {
        return new CsvMapReader<>(reader, consumer);
    }

    // -------------------- bean reader --------------------

    public <T> CsvBeanReader<T> mapReader(Class<T> targetClass) {
        return new CsvBeanReader<>(reader, targetClass);
    }

    public <T> CsvBeanReader<T> mapReader(Class<T> targetClass, List<T> rows) {
        return new CsvBeanReader<>(reader, targetClass, rows);
    }

    /**
     * 获取bean读取器
     *
     * @param consumer consumer
     * @param <T>      T
     * @return CsvBeanReader
     */
    public <T> CsvBeanReader<T> mapReader(Class<T> targetClass, Consumer<T> consumer) {
        return new CsvBeanReader<>(reader, targetClass, consumer);
    }

    // -------------------- raw reader --------------------

    public CsvRawReader rawReader() {
        return new CsvRawReader(reader);
    }

    public CsvRawReader rawReader(List<String> rows) {
        return new CsvRawReader(reader, rows);
    }

    /**
     * 获取raw读取器
     *
     * @param consumer consumer
     * @return CsvRawReader
     */
    public CsvRawReader rawReader(Consumer<String> consumer) {
        return new CsvRawReader(reader, consumer);
    }

    public CsvReader getReader() {
        return reader;
    }

    /**
     * 关闭读取流
     */
    @Override
    public void close() {
        reader.close();
    }

}
