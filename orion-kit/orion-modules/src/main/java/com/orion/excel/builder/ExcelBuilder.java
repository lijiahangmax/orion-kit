package com.orion.excel.builder;

import com.orion.utils.Streams;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Excel 构建器 不支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 21:59
 */
public class ExcelBuilder {

    /**
     * getter方法实例
     */
    private static final Map<Class<?>, List<Method>> ALL_GET_METHOD_CACHE = new ConcurrentHashMap<>(16);

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

    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * 写入到流
     *
     * @param out out
     * @throws IOException IOException
     */
    public void write(OutputStream out) throws IOException {
        write(out, false);
    }

    /**
     * 写入到文件
     *
     * @param file file
     * @throws IOException IOException
     */
    public void write(String file) throws IOException {
        Valid.notNull(file, "file is null");
        Files1.touch(file);
        write(Files1.openOutputStream(file), true);
    }

    /**
     * 写入到文件
     *
     * @param file file
     * @throws IOException IOException
     */
    public void write(File file) throws IOException {
        Valid.notNull(file, "file is null");
        Files1.touch(file);
        write(Files1.openOutputStream(file), true);
    }

    /**
     * 写入到文件
     *
     * @param out   out
     * @param close close
     * @throws IOException IOException
     */
    private void write(OutputStream out, boolean close) throws IOException {
        try {
            workbook.write(out);
        } finally {
            if (close) {
                Streams.closeQuietly(out);
            }
        }
    }

    /**
     * 获取所有getter方法
     *
     * @param clazz class
     * @return method
     */
    static List<Method> getAllGetterMethod(Class clazz) {
        List<Method> methodList = ALL_GET_METHOD_CACHE.get(clazz);
        if (methodList == null) {
            methodList = Methods.getAllGetterMethod(clazz);
            ALL_GET_METHOD_CACHE.put(clazz, methodList);
        }
        return methodList;
    }

}
