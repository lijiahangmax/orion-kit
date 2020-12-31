package com.orion.excel.builder;

import com.orion.able.SafeCloseable;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Excel 构建器 不支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 21:59
 */
public class ExcelBuilder implements SafeCloseable {

    /**
     * workbook
     */
    private Workbook workbook;

    public ExcelBuilder() {
        this.workbook = new SXSSFWorkbook();
    }

    public ExcelBuilder(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * 获取sheet
     *
     * @param <T>   T
     * @param index index
     * @return ExcelSheet
     */
    public <T> ExcelSheet<T> getSheet(int index) {
        return new ExcelSheet<>(workbook, workbook.getSheetAt(index));
    }

    /**
     * 获取sheet
     *
     * @param <T>          T
     * @param index        index
     * @param elementClass class
     * @return ExcelSheet
     * @see ExcelSheet#addRows
     */
    public <T> ExcelSheet<T> getSheet(int index, Class<? extends T> elementClass) {
        return new ExcelSheet<>(workbook, workbook.getSheetAt(index), elementClass);
    }

    /**
     * 获取sheet
     *
     * @param <T> T
     * @return ExcelSheet
     */
    public <T> ExcelSheet<T> newSheet() {
        return new ExcelSheet<>(workbook, workbook.createSheet());
    }

    /**
     * 获取sheet
     *
     * @param name sheet name
     * @param <T>  T
     * @return ExcelSheet
     */
    public <T> ExcelSheet<T> newSheet(String name) {
        return new ExcelSheet<>(workbook, workbook.createSheet(name));
    }

    /**
     * 获取sheet
     *
     * @param <T> T
     * @return ExcelSheet
     * @see ExcelSheet#addRows
     */
    public <T> ExcelSheet<T> newSheet(Class<? extends T> elementClass) {
        return new ExcelSheet<>(workbook, workbook.createSheet(), elementClass);
    }

    /**
     * 获取sheet
     *
     * @param name sheet name
     * @param <T>  T
     * @return ExcelSheet
     * @see ExcelSheet#addRows
     */
    public <T> ExcelSheet<T> newSheet(String name, Class<? extends T> elementClass) {
        return new ExcelSheet<>(workbook, workbook.createSheet(name), elementClass);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * 写入到流
     *
     * @param out out
     */
    public void write(OutputStream out) {
        try {
            workbook.write(out);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 写入到文件
     *
     * @param file file
     */
    public void write(String file) {
        Valid.notNull(file, "file is null");
        this.write(new File(file));
    }

    /**
     * 写入到文件
     *
     * @param file file
     */
    public void write(File file) {
        Valid.notNull(file, "file is null");
        Files1.touch(file);
        FileOutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(out);
        }
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

}
