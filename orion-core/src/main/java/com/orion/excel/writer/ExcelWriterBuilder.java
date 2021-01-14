package com.orion.excel.writer;

import com.orion.able.SafeCloseable;
import com.orion.excel.Excels;
import com.orion.excel.option.PropertiesOption;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;

/**
 * Excel 构建器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 21:59
 */
public class ExcelWriterBuilder implements SafeCloseable {

    /**
     * workbook
     */
    private Workbook workbook;

    public ExcelWriterBuilder() {
        this.workbook = new SXSSFWorkbook();
    }

    public ExcelWriterBuilder(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * 获取array writer
     *
     * @param <T>   T
     * @param index index
     * @return ExcelArrayWriter
     */
    public <T> ExcelArrayWriter<T> getArrayWriter(int index) {
        return new ExcelArrayWriter<>(workbook, workbook.getSheetAt(index));
    }

    /**
     * 获取array writer
     *
     * @param <T>  T
     * @param name name
     * @return ExcelArrayWriter
     */
    public <T> ExcelArrayWriter<T> getArrayWriter(String name) {
        return new ExcelArrayWriter<>(workbook, workbook.getSheet(name));
    }

    /**
     * 获取map writer
     *
     * @param <K>   K
     * @param <V>   V
     * @param index index
     * @return ExcelMapWriter
     */
    public <K, V> ExcelMapWriter<K, V> getMapWriter(int index) {
        return new ExcelMapWriter<>(workbook, workbook.getSheetAt(index));
    }

    /**
     * 获取map writer
     *
     * @param <K>  K
     * @param <V>  V
     * @param name name
     * @return ExcelMapWriter
     */
    public <K, V> ExcelMapWriter<K, V> getMapWriter(String name) {
        return new ExcelMapWriter<>(workbook, workbook.getSheet(name));
    }

    /**
     * 获取bean writer
     *
     * @param <T>         T
     * @param index       index
     * @param targetClass targetClass
     * @return ExcelBeanWriter
     */
    public <T> ExcelBeanWriter<T> getBeanWriter(int index, Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.getSheetAt(index), targetClass);
    }

    /**
     * 获取bean writer
     *
     * @param <T>         T
     * @param name        name
     * @param targetClass targetClass
     * @return ExcelBeanWriter
     */
    public <T> ExcelBeanWriter<T> getBeanWriter(String name, Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.getSheet(name), targetClass);
    }

    /**
     * 创建array writer
     *
     * @param <T> T
     * @return ExcelArrayWriter
     */
    public <T> ExcelArrayWriter<T> createArrayWriter() {
        return new ExcelArrayWriter<>(workbook, workbook.createSheet());
    }

    /**
     * 创建array writer
     *
     * @param <T>  T
     * @param name name
     * @return ExcelArrayWriter
     */
    public <T> ExcelArrayWriter<T> createArrayWriter(String name) {
        return new ExcelArrayWriter<>(workbook, workbook.createSheet(name));
    }

    /**
     * 创建map writer
     *
     * @param <K> K
     * @param <V> V
     * @return ExcelMapWriter
     */
    public <K, V> ExcelMapWriter<K, V> createMapWriter() {
        return new ExcelMapWriter<>(workbook, workbook.createSheet());
    }

    /**
     * 创建map writer
     *
     * @param <K>  K
     * @param <V>  V
     * @param name name
     * @return ExcelMapWriter
     */
    public <K, V> ExcelMapWriter<K, V> createMapWriter(String name) {
        return new ExcelMapWriter<>(workbook, workbook.createSheet(name));
    }

    /**
     * 创建bean writer
     *
     * @param <T>         T
     * @param targetClass targetClass
     * @return ExcelBeanWriter
     */
    public <T> ExcelBeanWriter<T> createBeanWriter(Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.createSheet(), targetClass);
    }

    /**
     * 创建bean writer
     *
     * @param <T>         T
     * @param name        name
     * @param targetClass targetClass
     * @return ExcelBeanWriter
     */
    public <T> ExcelBeanWriter<T> createBeanWriter(String name, Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.createSheet(name), targetClass);
    }

    /**
     * 设置表格属性
     *
     * @param option option
     * @return this
     */
    public ExcelWriterBuilder properties(PropertiesOption option) {
        Excels.setProperties(workbook, option);
        return this;
    }

    /**
     * 写入到流
     *
     * @param out out
     * @return this
     */
    public ExcelWriterBuilder write(OutputStream out) {
        Excels.write(workbook, out);
        return this;
    }

    /**
     * 写入到文件
     *
     * @param file file
     * @return this
     */
    public ExcelWriterBuilder write(String file) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写入到文件
     *
     * @param file file
     * @return this
     */
    public ExcelWriterBuilder write(File file) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写入到流
     *
     * @param password password
     * @param out      out
     * @return this
     */
    public ExcelWriterBuilder write(OutputStream out, String password) {
        Excels.write(workbook, out, password);
        return this;
    }

    /**
     * 写入到文件
     *
     * @param password password
     * @param file     file
     * @return this
     */
    public ExcelWriterBuilder write(String file, String password) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file, password);
        return this;
    }

    /**
     * 写入到文件
     *
     * @param password password
     * @param file     file
     * @return this
     */
    public ExcelWriterBuilder write(File file, String password) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file, password);
        return this;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

}
