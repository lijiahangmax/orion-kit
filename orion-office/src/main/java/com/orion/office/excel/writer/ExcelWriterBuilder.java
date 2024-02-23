package com.orion.office.excel.writer;

import com.orion.office.excel.Excels;
import com.orion.office.excel.option.PropertiesOption;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * excel 构建器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/6 21:59
 */
public class ExcelWriterBuilder extends BaseExcelWriteable {

    public ExcelWriterBuilder() {
        this(new SXSSFWorkbook());
    }

    public ExcelWriterBuilder(Workbook workbook) {
        super(workbook);
    }

    /**
     * 创建写入实例
     *
     * @return ExcelWriterBuilder
     */
    public static ExcelWriterBuilder create() {
        return new ExcelWriterBuilder();
    }

    /**
     * 创建写入实例
     *
     * @param workbook workbook
     * @return ExcelWriterBuilder
     */
    public static ExcelWriterBuilder create(Workbook workbook) {
        return new ExcelWriterBuilder(workbook);
    }

    public <T> ExcelArrayWriter<T> getArrayWriter(String name) {
        return new ExcelArrayWriter<>(workbook, workbook.getSheet(name));
    }

    /**
     * 获取 array writer
     *
     * @param <T>   T
     * @param index index
     * @return ExcelArrayWriter
     */
    public <T> ExcelArrayWriter<T> getArrayWriter(int index) {
        return new ExcelArrayWriter<>(workbook, workbook.getSheetAt(index));
    }

    public <K, V> ExcelMapWriter<K, V> getMapWriter(String name) {
        return new ExcelMapWriter<>(workbook, workbook.getSheet(name));
    }

    /**
     * 获取 map writer
     *
     * @param <K>   K
     * @param <V>   V
     * @param index index
     * @return ExcelMapWriter
     */
    public <K, V> ExcelMapWriter<K, V> getMapWriter(int index) {
        return new ExcelMapWriter<>(workbook, workbook.getSheetAt(index));
    }

    public <T> ExcelBeanWriter<T> getBeanWriter(String name, Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.getSheet(name), targetClass);
    }

    /**
     * 获取 bean writer
     *
     * @param <T>         T
     * @param index       index
     * @param targetClass targetClass
     * @return ExcelBeanWriter
     */
    public <T> ExcelBeanWriter<T> getBeanWriter(int index, Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.getSheetAt(index), targetClass);
    }

    public <T> ExcelLambdaWriter<T> getLambdaWriter(String name) {
        return new ExcelLambdaWriter<>(workbook, workbook.getSheet(name));
    }

    /**
     * 获取 lambda writer
     *
     * @param <T>   T
     * @param index index
     * @return ExcelLambdaWriter
     */
    public <T> ExcelLambdaWriter<T> getLambdaWriter(int index) {
        return new ExcelLambdaWriter<>(workbook, workbook.getSheetAt(index));
    }

    public <T> ExcelArrayWriter<T> createArrayWriter() {
        return new ExcelArrayWriter<>(workbook, workbook.createSheet());
    }

    /**
     * 创建 array writer
     *
     * @param <T>  T
     * @param name name
     * @return ExcelArrayWriter
     */
    public <T> ExcelArrayWriter<T> createArrayWriter(String name) {
        return new ExcelArrayWriter<>(workbook, workbook.createSheet(name));
    }

    public <K, V> ExcelMapWriter<K, V> createMapWriter() {
        return new ExcelMapWriter<>(workbook, workbook.createSheet());
    }

    /**
     * 创建 map writer
     *
     * @param <K>  K
     * @param <V>  V
     * @param name name
     * @return ExcelMapWriter
     */
    public <K, V> ExcelMapWriter<K, V> createMapWriter(String name) {
        return new ExcelMapWriter<>(workbook, workbook.createSheet(name));
    }

    public <T> ExcelBeanWriter<T> createBeanWriter(Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.createSheet(), targetClass);
    }

    /**
     * 创建 bean writer
     *
     * @param <T>         T
     * @param name        name
     * @param targetClass targetClass
     * @return ExcelBeanWriter
     */
    public <T> ExcelBeanWriter<T> createBeanWriter(String name, Class<T> targetClass) {
        return new ExcelBeanWriter<>(workbook, workbook.createSheet(name), targetClass);
    }

    public <T> ExcelLambdaWriter<T> createLambdaWriter() {
        return new ExcelLambdaWriter<>(workbook, workbook.createSheet());
    }

    /**
     * 创建 lambda writer
     *
     * @param <T>  T
     * @param name name
     * @return ExcelLambdaWriter
     */
    public <T> ExcelLambdaWriter<T> createLambdaWriter(String name) {
        return new ExcelLambdaWriter<>(workbook, workbook.createSheet(name));
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

}
