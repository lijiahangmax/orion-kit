package com.orion.office.excel.writer;

import com.orion.office.excel.Excels;
import com.orion.office.excel.option.PropertiesOption;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Excel 构建器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/6 21:59
 */
public class ExcelWriterBuilder extends BaseExcelWriteable {

    /**
     * workbook
     */
    private Workbook workbook;

    public ExcelWriterBuilder() {
        this(new SXSSFWorkbook());
    }

    public ExcelWriterBuilder(Workbook workbook) {
        super(workbook);
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

    public Workbook getWorkbook() {
        return workbook;
    }

}
