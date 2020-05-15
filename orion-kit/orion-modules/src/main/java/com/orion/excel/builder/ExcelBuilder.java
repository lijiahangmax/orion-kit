package com.orion.excel.builder;

import com.orion.able.Builderable;
import com.orion.utils.Streams;
import com.orion.utils.Valid;
import com.orion.utils.file.Files1;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Excel 构建器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 21:59
 */
public class ExcelBuilder implements Builderable<Workbook> {

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

    /**
     * 获取sheet
     *
     * @param <T> T
     * @return ExcelSheet
     */
    public <T> ExcelSheet<T> newSheet() {
        return new ExcelSheet<>(workbook.createSheet());
    }

    /**
     * 获取sheet
     *
     * @param name sheet name
     * @param <T>  T
     * @return ExcelSheet
     */
    public <T> ExcelSheet<T> newSheet(String name) {
        return new ExcelSheet<>(workbook.createSheet(name));
    }

    @Override
    public Workbook build() {
        return workbook;
    }

    /**
     * 写入到流
     *
     * @param out out
     * @throws IOException IOException
     */
    public void write(OutputStream out) throws IOException {
        workbook.write(out);
    }

    /**
     * 写入到文件
     *
     * @param file file
     * @throws IOException IOException
     */
    public void write(String file) throws IOException {
        write(new File(file));
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
        FileOutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
            workbook.write(out);
        } finally {
            Streams.closeQuietly(out);
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
