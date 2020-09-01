package com.orion.excel.importing;

import com.monitorjbl.xlsx.impl.StreamingSheet;
import com.orion.excel.Excels;
import com.orion.excel.annotation.ImportField;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.reflect.Constructors;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 读取器 不支持随机读写 仅支持注解
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/27 13:52
 */
public class ExcelImport<T> {

    /**
     * 表
     */
    private Sheet sheet;

    /**
     * bean class
     */
    private Class<T> targetClass;

    /**
     * bean constructor
     */
    private Constructor<T> constructor;

    /**
     * bean setter methods
     */
    private Map<Integer, Method> setMethods = new HashMap<>();

    /**
     * 行数
     */
    private int rowNum;

    /**
     * 读取行数
     */
    private int readRowNum;

    /**
     * 行索引
     */
    private int rowIndex;

    /**
     * 是否跳过空行
     */
    private boolean skipNullRows = true;

    /**
     * 是否为流式读取
     */
    private boolean streaming;

    /**
     * 读取的记录
     */
    private List<T> rows = new ArrayList<>();

    public ExcelImport(Sheet sheet, Class<T> targetClass) {
        Valid.notNull(sheet, "Sheet is null");
        Valid.notNull(targetClass, "TargetClass is null");
        this.sheet = sheet;
        this.streaming = sheet instanceof StreamingSheet;
        this.rowNum = sheet.getLastRowNum() + 1;
        this.targetClass = targetClass;
        this.constructor = Constructors.getDefaultConstructor(targetClass);
        if (constructor == null) {
            throw Exceptions.argument("targetClass not found default constructor");
        }
        this.analysisClass();
    }

    /**
     * 读取一行
     *
     * @return this
     */
    public ExcelImport<T> readRow() {
        readRowNum += 1;
        return this;
    }

    /**
     * 读取多行
     *
     * @param i 读取行数
     * @return this
     */
    public ExcelImport<T> readRows(int i) {
        readRowNum += i;
        return this;
    }

    /**
     * 读取多行
     *
     * @return this
     */
    public ExcelImport<T> readRows() {
        readRowNum = 0;
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public ExcelImport<T> skipRow() {
        rowIndex++;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param i 跳过的行数
     * @return this
     */
    public ExcelImport<T> skipRows(int i) {
        rowIndex += i;
        return this;
    }

    /**
     * 是否跳过空行
     *
     * @param skip 如果row为null true: continue false: rows.add(null), row = null
     * @return this
     */
    public ExcelImport<T> skipNullRows(boolean skip) {
        this.skipNullRows = skip;
        return this;
    }

    /**
     * 解析 setter method
     */
    private void analysisClass() {
        // 扫描setter
        List<Method> setterMethodList = Methods.getAllSetterMethod(this.targetClass);
        // 扫描field
        List<Field> fieldList = Fields.getFieldList(this.targetClass);
        for (Field field : fieldList) {
            ImportField importField = field.getAnnotation(ImportField.class);
            if (importField != null) {
                int columnIndex = importField.value();
                if (columnIndex != -1) {
                    Method method = Methods.getSetterMethodByField(this.targetClass, field);
                    if (method != null) {
                        setMethods.put(columnIndex, method);
                    }
                }
            }
        }
        for (Method method : setterMethodList) {
            ImportField importField = method.getAnnotation(ImportField.class);
            if (importField != null) {
                int columnIndex = importField.value();
                if (columnIndex != -1) {
                    setMethods.put(columnIndex, method);
                }
            }
        }
    }

    /**
     * row -> bean
     *
     * @param row row
     */
    private void addBean(Row row) {
        if (row == null) {
            if (!skipNullRows) {
                rows.add(Constructors.newInstance(constructor));
            }
            return;
        }

        T t = Constructors.newInstance(constructor);
        for (Map.Entry<Integer, Method> entry : setMethods.entrySet()) {
            Method setMethod = entry.getValue();
            if (setMethod == null) {
                continue;
            }
            String value = Excels.getValue(row.getCell(entry.getKey()));
            try {
                Methods.invokeMethodInfer(t, setMethod, new Object[]{value});
            } catch (Exception e) {
                // ignore
            }
        }
        rows.add(t);
    }

    /**
     * 执行导入
     *
     * @return this
     */
    public ExcelImport<T> execute() {
        if (readRowNum == 0 || readRowNum > rowNum) {
            readRowNum = rowNum;
        }
        if (rowIndex != 0 && rowNum != readRowNum) {
            readRowNum += rowIndex;
            if (readRowNum > rowNum) {
                readRowNum = rowNum;
            }
        }
        if (streaming) {
            // 流式读取
            int i = 0;
            for (Row row : sheet) {
                if (i++ < rowIndex) {
                    continue;
                }
                if (rowIndex++ < readRowNum) {
                    this.addBean(row);
                }
            }
        } else {
            for (; rowIndex < readRowNum; ) {
                this.addBean(sheet.getRow(rowIndex++));
            }
        }
        return this;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public List<T> rows() {
        return rows;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public boolean isSkipNullRows() {
        return skipNullRows;
    }

}
