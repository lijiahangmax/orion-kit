package com.orion.excel;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingWorkbook;
import com.orion.dom.DomExt;
import com.orion.excel.importing.ExcelImport;
import com.orion.excel.importing.ExcelReader;
import com.orion.excel.importing.ExcelStream;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Excel 提取器
 * <p>
 * 使用流式读取文件必须是 xlsx 文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 12:51
 */
public class ExcelExt {

    private Workbook workbook;

    /**
     * 是否为流式读取
     */
    private boolean streaming;

    /**
     * 流式读取缓存行数
     */
    private static final int STREAMING_ROW = 100;

    /**
     * 流式读取缓冲区大小
     */
    private static final int STREAMING_BUFFER = 4096;

    public ExcelExt(File file) {
        this(Files1.openInputStreamSafe(file), file.getName(), false);
    }

    public ExcelExt(File file, boolean streaming) {
        this(Files1.openInputStreamSafe(file), file.getName(), streaming);
    }

    public ExcelExt(String file) {
        this(DomExt.class.getClassLoader().getResourceAsStream(file), null, false);
    }

    public ExcelExt(String file, boolean streaming) {
        this(DomExt.class.getClassLoader().getResourceAsStream(file), null, streaming);
    }

    public ExcelExt(InputStream in) {
        this(in, null, false);
    }

    public ExcelExt(InputStream in, boolean streaming) {
        this(in, null, streaming);
    }

    public ExcelExt(InputStream in, String name, boolean streaming) {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 4 * 1024);
        }
        // 使用流式读取文件必须是 xlsx 文件
        if (streaming) {
            if (name != null && name.toLowerCase().endsWith("xls")) {
                throw Exceptions.parse("Cannot using streaming open 2003 workbook");
            }
            this.workbook = StreamingReader.builder()
                    .rowCacheSize(STREAMING_ROW)
                    .bufferSize(STREAMING_BUFFER)
                    .open(in);
        } else {
            try {
                if (name != null && name.toLowerCase().endsWith("xls")) {
                    this.workbook = new HSSFWorkbook(in);
                } else {
                    OPCPackage p = OPCPackage.open(in);
                    this.workbook = new XSSFWorkbook(p);
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            } catch (Exception e) {
                throw Exceptions.parse("Cannot open this workbook error: " + e.getMessage());
            }
        }
        Valid.notNull(workbook, "workbook is null");
        this.streaming = streaming;
        Streams.close(in);
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
            throw Exceptions.unsupport("ExcelReader can't be used streaming");
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
            throw Exceptions.unsupport("ExcelReader can't be used streaming");
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
            throw Exceptions.unsupport("ExcelReader can't be used streaming");
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
            throw Exceptions.unsupport("ExcelReader can't be used streaming");
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
        return new ExcelImport<>(workbook.getSheetAt(0), targetClass);
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
        return new ExcelImport<>(workbook.getSheetAt(sheetIndex), targetClass);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void close() {
        Streams.close(workbook);
    }

}
