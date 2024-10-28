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
package cn.orionsec.kit.office.excel;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.define.collect.MutableMap;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.office.excel.reader.ExcelArrayReader;
import cn.orionsec.kit.office.excel.reader.ExcelBeanReader;
import cn.orionsec.kit.office.excel.reader.ExcelLambdaReader;
import cn.orionsec.kit.office.excel.reader.ExcelMapReader;
import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * excel 提取器
 * <p>
 * 使用流式读取文件必须是 xlsx 文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/6 12:51
 */
public class ExcelExt implements SafeCloseable {

    private final Workbook workbook;

    public ExcelExt(File file) {
        this(Files1.openInputStreamSafe(file), null, false, true);
    }

    public ExcelExt(File file, boolean streaming) {
        this(Files1.openInputStreamSafe(file), null, streaming, true);
    }

    public ExcelExt(File file, String password) {
        this(Files1.openInputStreamSafe(file), password, false, true);
    }

    public ExcelExt(File file, String password, boolean streaming) {
        this(Files1.openInputStreamSafe(file), password, streaming, true);
    }

    public ExcelExt(String file) {
        this(Files1.openInputStreamSafe(file), null, false, true);
    }

    public ExcelExt(String file, boolean streaming) {
        this(Files1.openInputStreamSafe(file), null, streaming, true);
    }

    public ExcelExt(String file, String password) {
        this(Files1.openInputStreamSafe(file), password, false, true);
    }

    public ExcelExt(String file, String password, boolean streaming) {
        this(Files1.openInputStreamSafe(file), password, streaming, true);
    }

    public ExcelExt(InputStream in) {
        this(in, null, false, false);
    }

    public ExcelExt(InputStream in, boolean streaming) {
        this(in, null, streaming, false);
    }

    public ExcelExt(InputStream in, String password) {
        this(in, password, false, false);
    }

    public ExcelExt(InputStream in, String password, boolean streaming) {
        this(in, password, streaming, false);
    }

    public ExcelExt(InputStream in, String password, boolean streaming, boolean close) {
        if (streaming) {
            // 使用流式读取文件必须是 xlsx 文件
            this.workbook = Excels.openStreamingWorkbook(in, password);
        } else {
            this.workbook = Excels.openWorkbook(in, password);
        }
        Valid.notNull(workbook, "workbook is null");
        if (close) {
            Streams.close(in);
        }
    }

    public ExcelExt(Workbook workbook) {
        Valid.notNull(workbook, "workbook is null");
        this.workbook = workbook;
    }

    // -------------------- array reader --------------------

    public ExcelArrayReader arrayReader(int sheetIndex) {
        return this.arrayReader(workbook.getSheetAt(sheetIndex), new ArrayList<>(), null);
    }

    public ExcelArrayReader arrayReader(String sheetName) {
        return this.arrayReader(workbook.getSheet(sheetName), new ArrayList<>(), null);
    }

    public ExcelArrayReader arrayReader(int sheetIndex, List<String[]> rows) {
        return this.arrayReader(workbook.getSheetAt(sheetIndex), rows, null);
    }

    public ExcelArrayReader arrayReader(String sheetName, List<String[]> rows) {
        return this.arrayReader(workbook.getSheet(sheetName), rows, null);
    }

    public ExcelArrayReader arrayReader(int sheetIndex, Consumer<String[]> consumer) {
        return this.arrayReader(workbook.getSheetAt(sheetIndex), null, consumer);
    }

    public ExcelArrayReader arrayReader(String sheetName, Consumer<String[]> consumer) {
        return this.arrayReader(workbook.getSheet(sheetName), null, consumer);
    }

    /**
     * 获取表格的 array 读取器
     *
     * @param sheet    sheet
     * @param rows     rows
     * @param consumer consumer
     * @return ExcelArrayReader
     */
    private ExcelArrayReader arrayReader(Sheet sheet, List<String[]> rows, Consumer<String[]> consumer) {
        if (rows != null) {
            return new ExcelArrayReader(workbook, sheet, rows);
        } else {
            return new ExcelArrayReader(workbook, sheet, consumer);
        }
    }

    // -------------------- map reader --------------------

    public <K, V> ExcelMapReader<K, V> mapReader(int sheetIndex) {
        return this.mapReader(workbook.getSheetAt(sheetIndex), new ArrayList<>(), null);
    }

    public <K, V> ExcelMapReader<K, V> mapReader(String sheetName) {
        return this.mapReader(workbook.getSheet(sheetName), new ArrayList<>(), null);
    }

    public <K, V> ExcelMapReader<K, V> mapReader(int sheetIndex, List<MutableMap<K, V>> rows) {
        return this.mapReader(workbook.getSheetAt(sheetIndex), rows, null);
    }

    public <K, V> ExcelMapReader<K, V> mapReader(String sheetName, List<MutableMap<K, V>> rows) {
        return this.mapReader(workbook.getSheet(sheetName), rows, null);
    }

    public <K, V> ExcelMapReader<K, V> mapReader(int sheetIndex, Consumer<MutableMap<K, V>> consumer) {
        return this.mapReader(workbook.getSheetAt(sheetIndex), null, consumer);
    }

    public <K, V> ExcelMapReader<K, V> mapReader(String sheetName, Consumer<MutableMap<K, V>> consumer) {
        return this.mapReader(workbook.getSheet(sheetName), null, consumer);
    }

    /**
     * 获取表格的 map 读取器
     *
     * @param sheet    sheet
     * @param rows     rows
     * @param consumer consumer
     * @param <K>      K
     * @param <V>      V
     * @return ExcelMapReader
     */
    private <K, V> ExcelMapReader<K, V> mapReader(Sheet sheet, List<MutableMap<K, V>> rows, Consumer<MutableMap<K, V>> consumer) {
        if (rows != null) {
            return new ExcelMapReader<>(workbook, sheet, rows);
        } else {
            return new ExcelMapReader<>(workbook, sheet, consumer);
        }
    }

    // -------------------- bean reader --------------------

    public <T> ExcelBeanReader<T> beanReader(int sheetIndex, Class<T> targetClass) {
        return this.beanReader(workbook.getSheetAt(sheetIndex), targetClass, new ArrayList<>(), null);
    }

    public <T> ExcelBeanReader<T> beanReader(String sheetName, Class<T> targetClass) {
        return this.beanReader(workbook.getSheet(sheetName), targetClass, new ArrayList<>(), null);
    }

    public <T> ExcelBeanReader<T> beanReader(int sheetIndex, Class<T> targetClass, List<T> rows) {
        return this.beanReader(workbook.getSheetAt(sheetIndex), targetClass, rows, null);
    }

    public <T> ExcelBeanReader<T> beanReader(String sheetName, Class<T> targetClass, List<T> rows) {
        return this.beanReader(workbook.getSheet(sheetName), targetClass, rows, null);
    }

    public <T> ExcelBeanReader<T> beanReader(int sheetIndex, Class<T> targetClass, Consumer<T> consumer) {
        return this.beanReader(workbook.getSheetAt(sheetIndex), targetClass, null, consumer);
    }

    public <T> ExcelBeanReader<T> beanReader(String sheetName, Class<T> targetClass, Consumer<T> consumer) {
        return this.beanReader(workbook.getSheet(sheetName), targetClass, null, consumer);
    }

    /**
     * 获取表格的 bean 读取器
     *
     * @param sheet       sheet
     * @param targetClass bean class
     * @param rows        rows
     * @param consumer    consumer
     * @param <T>         T
     * @return ExcelBeanReader
     */
    private <T> ExcelBeanReader<T> beanReader(Sheet sheet, Class<T> targetClass, List<T> rows, Consumer<T> consumer) {
        if (rows != null) {
            return new ExcelBeanReader<>(workbook, sheet, targetClass, rows);
        } else {
            return new ExcelBeanReader<>(workbook, sheet, targetClass, consumer);
        }
    }

    // -------------------- lambda reader --------------------

    public <T> ExcelLambdaReader<T> lambdaReader(int sheetIndex, Supplier<T> supplier) {
        return this.lambdaReader(workbook.getSheetAt(sheetIndex), new ArrayList<>(), null, supplier);
    }

    public <T> ExcelLambdaReader<T> lambdaReader(String sheetName, Supplier<T> supplier) {
        return this.lambdaReader(workbook.getSheet(sheetName), new ArrayList<>(), null, supplier);
    }

    public <T> ExcelLambdaReader<T> lambdaReader(int sheetIndex, List<T> rows, Supplier<T> supplier) {
        return this.lambdaReader(workbook.getSheetAt(sheetIndex), rows, null, supplier);
    }

    public <T> ExcelLambdaReader<T> lambdaReader(String sheetName, List<T> rows, Supplier<T> supplier) {
        return this.lambdaReader(workbook.getSheet(sheetName), rows, null, supplier);
    }

    public <T> ExcelLambdaReader<T> lambdaReader(int sheetIndex, Consumer<T> consumer, Supplier<T> supplier) {
        return this.lambdaReader(workbook.getSheetAt(sheetIndex), null, consumer, supplier);
    }

    public <T> ExcelLambdaReader<T> lambdaReader(String sheetName, Consumer<T> consumer, Supplier<T> supplier) {
        return this.lambdaReader(workbook.getSheet(sheetName), null, consumer, supplier);
    }

    /**
     * 获取表格的 lambda 读取器
     *
     * @param sheet    sheet
     * @param rows     rows
     * @param consumer consumer
     * @param supplier supplier
     * @param <T>      T
     * @return ExcelLambdaReader
     */
    private <T> ExcelLambdaReader<T> lambdaReader(Sheet sheet, List<T> rows, Consumer<T> consumer, Supplier<T> supplier) {
        if (rows != null) {
            return new ExcelLambdaReader<>(workbook, sheet, rows, supplier);
        } else {
            return new ExcelLambdaReader<>(workbook, sheet, consumer, supplier);
        }
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public boolean isStreaming() {
        return workbook instanceof StreamingWorkbook;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

}
