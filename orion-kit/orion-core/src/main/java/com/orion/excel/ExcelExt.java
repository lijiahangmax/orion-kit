package com.orion.excel;

import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import com.orion.ExcelReader;
import com.orion.ExcelStream;
import com.orion.able.SafeCloseable;
import com.orion.excel.importing.ExcelImport;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;

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

    /**
     * 是否为流式读取
     */
    private boolean streaming;

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
        this.streaming = streaming;
        if (close) {
            Streams.close(in);
        }
    }

    public ExcelExt(Workbook workbook) {
        Valid.notNull(workbook, "workbook is null");
        this.workbook = workbook;
        this.streaming = workbook instanceof StreamingWorkbook;
    }

    /**
     * 获取第一个表格的读取器 不支持流式读取 支持随机读写 不支持注解
     *
     * @return 流
     */
    public ExcelReader sheetReader() {
        if (streaming) {
            throw Exceptions.unSupport("ExcelReader can't be used streaming");
        }
        return new ExcelReader(workbook.getSheetAt(0));
    }

    /**
     * 获取第一个表格的读取器 不支持流式读取 支持随机读写 不支持注解
     *
     * @param columns 读取的列
     * @return 流
     */
    public ExcelReader sheetReader(int[] columns) {
        if (streaming) {
            throw Exceptions.unSupport("ExcelReader can't be used streaming");
        }
        return new ExcelReader(workbook.getSheetAt(0), columns);
    }

    /**
     * 获取表格的读取器 不支持流式读取 支持随机读写 不支持注解
     *
     * @param sheetIndex 表格索引
     * @return 流
     */
    public ExcelReader sheetReader(int sheetIndex) {
        if (streaming) {
            throw Exceptions.unSupport("ExcelReader can't be used streaming");
        }
        return new ExcelReader(workbook.getSheetAt(sheetIndex));
    }

    /**
     * 获取表格的读取器 不支持流式读取 支持随机读写 不支持注解
     *
     * @param sheetIndex 表格索引
     * @param columns    读取的列
     * @return 流
     */
    public ExcelReader sheetReader(int sheetIndex, int[] columns) {
        if (streaming) {
            throw Exceptions.unSupport("ExcelReader can't be used streaming");
        }
        return new ExcelReader(workbook.getSheetAt(sheetIndex), columns);
    }

    /**
     * 获取第一个表格的流式读取器 不支持随机读写 不支持注解
     *
     * @return ExcelStream
     */
    public ExcelStream sheetStream() {
        return new ExcelStream(workbook.getSheetAt(0));
    }

    /**
     * 获取第一个表格的流式读取器 不支持随机读写 不支持注解
     *
     * @param columns 读取的列
     * @return ExcelStream
     */
    public ExcelStream sheetStream(int[] columns) {
        return new ExcelStream(workbook.getSheetAt(0), columns);
    }

    /**
     * 获取表格的流式读取器 不支持随机读写 不支持注解
     *
     * @param sheetIndex 表格索引
     * @return ExcelStream
     */
    public ExcelStream sheetStream(int sheetIndex) {
        return new ExcelStream(workbook.getSheetAt(sheetIndex));
    }

    /**
     * 获取表格的流式读取器 不支持随机读写 不支持注解
     *
     * @param sheetIndex 表格索引
     * @param columns    读取的列
     * @return ExcelStream
     */
    public ExcelStream sheetStream(int sheetIndex, int[] columns) {
        return new ExcelStream(workbook.getSheetAt(sheetIndex), columns);
    }

    /**
     * 获取第一个表格的读取器 不支持随机读写 仅支持注解
     *
     * @param targetClass bean class
     * @param <T>         T
     * @return ExcelImport
     */
    public <T> ExcelImport<T> sheetImport(Class<T> targetClass) {
        return new ExcelImport<>(workbook, workbook.getSheetAt(0), targetClass);
    }

    /**
     * 获取表格的读取器 不支持随机读写 仅支持注解
     *
     * @param sheetIndex  表格索引
     * @param targetClass bean class
     * @param <T>         T
     * @return ExcelImport
     */
    public <T> ExcelImport<T> sheetImport(int sheetIndex, Class<T> targetClass) {
        return new ExcelImport<>(workbook, workbook.getSheetAt(sheetIndex), targetClass);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public boolean isStreaming() {
        return streaming;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

}
