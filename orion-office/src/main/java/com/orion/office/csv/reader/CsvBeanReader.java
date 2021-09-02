package com.orion.office.csv.reader;

import com.orion.office.csv.annotation.ImportField;
import com.orion.office.csv.annotation.ImportIgnore;
import com.orion.office.csv.annotation.ImportSetting;
import com.orion.office.csv.core.CsvReader;
import com.orion.office.csv.option.CsvReaderOption;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.reflect.Annotations;
import com.orion.utils.reflect.Constructors;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Csv Bean 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/7 14:50
 */
public class CsvBeanReader<T> extends BaseCsvReader<T> {

    private Class<T> targetClass;

    private Constructor<T> constructor;

    /**
     * 如果列为null是否调用setter(null)
     */
    private boolean nullInvoke;

    /**
     * all setter
     */
    private Map<String, Method> setters;

    /**
     * 映射
     * key: column
     * value: valueKey
     */
    protected Map<Integer, String> mapping;

    public CsvBeanReader(CsvReader reader, Class<T> targetClass) {
        this(reader, targetClass, new ArrayList<>(), null);
    }

    public CsvBeanReader(CsvReader reader, Class<T> targetClass, List<T> rows) {
        this(reader, targetClass, rows, null);
    }

    public CsvBeanReader(CsvReader reader, Class<T> targetClass, Consumer<T> consumer) {
        this(reader, targetClass, null, consumer);
    }

    protected CsvBeanReader(CsvReader reader, Class<T> targetClass, List<T> rows, Consumer<T> consumer) {
        super(reader, rows, consumer);
        this.targetClass = Valid.notNull(targetClass, "target class is null");
        this.mapping = new TreeMap<>();
        this.parseClass();
        this.parseField();
    }

    /**
     * 如果列为null是否调用setter(null)
     *
     * @return this
     */
    public CsvBeanReader<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    /**
     * 映射
     *
     * @param field  field
     * @param column column
     * @return this
     */
    public CsvBeanReader<T> mapping(String field, int column) {
        return mapping(column, field);
    }

    public CsvBeanReader<T> mapping(int column, String field) {
        Method method = setters.get(field);
        if (method == null) {
            throw Exceptions.parse("not found " + field + " setter method");
        }
        mapping.put(column, field);
        return this;
    }

    @Override
    protected T parserRow(String[] row) {
        T t = Constructors.newInstance(constructor);
        mapping.forEach((k, v) -> {
            Method setter = setters.get(v);
            if (setter == null) {
                return;
            }
            Object value = this.get(row, k);
            // 执行setter
            if (value != null) {
                try {
                    Methods.invokeSetterInfer(t, setter, value);
                } catch (Exception e) {
                    // ignore
                }
            } else if (nullInvoke) {
                Methods.invokeMethod(t, setter, (Object) null);
            }
        });
        return t;
    }

    /**
     * 解析class
     */
    protected void parseClass() {
        this.constructor = Valid.notNull(Constructors.getDefaultConstructor(targetClass), "target class not found default constructor");
        ImportSetting setting = Annotations.getAnnotation(targetClass, ImportSetting.class);
        CsvReaderOption option = new CsvReaderOption();
        if (setting == null) {
            reader.setOption(option);
            return;
        }
        option.setCaseSensitive(setting.caseSensitive())
                .setUseComments(setting.useComments())
                .setSafetySwitch(setting.safetySwitch())
                .setSkipEmptyRows(setting.skipEmptyRows())
                .setSkipRawRow(setting.skipRawRow());
        option.setTextQualifier(setting.textQualifier())
                .setUseTextQualifier(setting.useTextQualifier())
                .setDelimiter(setting.delimiter())
                .setLineDelimiter(setting.lineDelimiter())
                .setComment(setting.comment())
                .setEscapeMode(setting.escapeMode())
                .setUseTextQualifier(setting.useTextQualifier())
                .setCharset(Charset.forName(setting.charset()))
                .setTrim(setting.trim());
        reader.setOption(option);
    }

    /**
     * 解析field
     */
    protected void parseField() {
        // 注解field
        List<Field> fieldList = Fields.getFieldsByCache(targetClass);
        // 注解method
        List<Method> methodList = Methods.getSetterMethodsByCache(targetClass);
        this.setters = methodList.stream().collect(Collectors.toMap(Fields::getFieldNameByMethod, Function.identity()));
        for (Field field : fieldList) {
            this.parseColumn(Annotations.getAnnotation(field, ImportField.class),
                    Annotations.getAnnotation(field, ImportIgnore.class),
                    Methods.getGetterMethodByField(targetClass, field), field.getName());
        }
        for (Method method : methodList) {
            this.parseColumn(Annotations.getAnnotation(method, ImportField.class),
                    Annotations.getAnnotation(method, ImportIgnore.class),
                    method, null);
        }
    }

    /**
     * 解析列
     *
     * @param field     field
     * @param ignore    ignore
     * @param method    getter
     * @param fieldName fieldName
     */
    protected void parseColumn(ImportField field, ImportIgnore ignore, Method method, String fieldName) {
        if (field == null || ignore != null) {
            return;
        }
        if (method == null) {
            throw Exceptions.parse("not found " + fieldName + "setter method");
        }
        int index = field.value();
        if (fieldName == null) {
            mapping.put(index, Fields.getFieldNameByMethod(method));
        } else {
            mapping.put(index, fieldName);
        }
    }

}
