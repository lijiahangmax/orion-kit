package com.orion.excel;

import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import com.orion.able.SafeCloseable;
import com.orion.excel.reader.ExcelArrayReader;
import com.orion.excel.reader.ExcelBeanReader;
import com.orion.excel.reader.ExcelMapReader;
import com.orion.lang.collect.MutableMap;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel 提取器
 * <p>
 * 使用流式读取文件必须是 xlsx 文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 12:51
 */
public class ExcelExt implements SafeCloseable {

    private Workbook workbook;

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

    private ExcelExt(InputStream in, String password, boolean streaming, boolean close) {
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

    public ExcelArrayReader arrayReader() {
        return new ExcelArrayReader(workbook, workbook.getSheetAt(0));
    }

    public ExcelArrayReader arrayReader(int sheetIndex) {
        return new ExcelArrayReader(workbook, workbook.getSheetAt(sheetIndex));
    }

    public ExcelArrayReader arrayReader(List<String[]> rows) {
        return new ExcelArrayReader(workbook, workbook.getSheetAt(0), rows);
    }

    public ExcelArrayReader arrayReader(int sheetIndex, List<String[]> rows) {
        return new ExcelArrayReader(workbook, workbook.getSheetAt(sheetIndex), rows);
    }

    /**
     * 获取第一个表格的array读取器
     *
     * @param consumer consumer
     * @return ExcelArrayReader
     */
    public ExcelArrayReader arrayReader(Consumer<String[]> consumer) {
        return new ExcelArrayReader(workbook, workbook.getSheetAt(0), consumer);
    }

    /**
     * 获取表格的array读取器
     *
     * @param sheetIndex 表格索引
     * @param consumer   consumer
     * @return ExcelArrayReader
     */
    public ExcelArrayReader arrayReader(int sheetIndex, Consumer<String[]> consumer) {
        return new ExcelArrayReader(workbook, workbook.getSheetAt(sheetIndex), consumer);
    }

    // -------------------- map reader --------------------

    public <K> ExcelMapReader<K> mapReader() {
        return new ExcelMapReader<>(workbook, workbook.getSheetAt(0));
    }

    public <K> ExcelMapReader<K> mapReader(int sheetIndex) {
        return new ExcelMapReader<>(workbook, workbook.getSheetAt(sheetIndex));
    }

    public <K> ExcelMapReader<K> mapReader(List<MutableMap<K, Object>> rows) {
        return new ExcelMapReader<>(workbook, workbook.getSheetAt(0), rows);
    }

    public <K> ExcelMapReader<K> mapReader(int sheetIndex, List<MutableMap<K, Object>> rows) {
        return new ExcelMapReader<>(workbook, workbook.getSheetAt(sheetIndex), rows);
    }

    /**
     * 获取第一个表格的map读取器
     *
     * @param consumer consumer
     * @param <K>      K
     * @return ExcelMapReader
     */
    public <K> ExcelMapReader<K> mapReader(Consumer<MutableMap<K, Object>> consumer) {
        return new ExcelMapReader<>(workbook, workbook.getSheetAt(0), consumer);
    }

    /**
     * 获取表格的map读取器
     *
     * @param sheetIndex 表格索引
     * @param consumer   consumer
     * @param <K>        K
     * @return ExcelMapReader
     */
    public <K> ExcelMapReader<K> mapReader(int sheetIndex, Consumer<MutableMap<K, Object>> consumer) {
        return new ExcelMapReader<>(workbook, workbook.getSheetAt(sheetIndex), consumer);
    }

    // -------------------- import --------------------

    public <T> ExcelBeanReader<T> sheetImport(Class<T> targetClass) {
        return new ExcelBeanReader<>(workbook, workbook.getSheetAt(0), targetClass);
    }

    public <T> ExcelBeanReader<T> sheetImport(int sheetIndex, Class<T> targetClass) {
        return new ExcelBeanReader<>(workbook, workbook.getSheetAt(sheetIndex), targetClass);
    }

    public <T> ExcelBeanReader<T> sheetImport(Class<T> targetClass, List<T> rows) {
        return new ExcelBeanReader<>(workbook, workbook.getSheetAt(0), targetClass, rows);
    }

    public <T> ExcelBeanReader<T> sheetImport(int sheetIndex, Class<T> targetClass, List<T> rows) {
        return new ExcelBeanReader<>(workbook, workbook.getSheetAt(sheetIndex), targetClass, rows);
    }

    /**
     * 获取第一个表格的读取器
     *
     * @param targetClass bean class
     * @param consumer    consumer
     * @param <T>         T
     * @return ExcelImport
     */
    public <T> ExcelBeanReader<T> sheetImport(Class<T> targetClass, Consumer<T> consumer) {
        return new ExcelBeanReader<>(workbook, workbook.getSheetAt(0), targetClass, consumer);
    }

    /**
     * 获取表格的读取器
     *
     * @param sheetIndex  表格索引
     * @param targetClass bean class
     * @param consumer    consumer
     * @param <T>         T
     * @return ExcelImport
     */
    public <T> ExcelBeanReader<T> sheetImport(int sheetIndex, Class<T> targetClass, Consumer<T> consumer) {
        return new ExcelBeanReader<>(workbook, workbook.getSheetAt(sheetIndex), targetClass, consumer);
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
