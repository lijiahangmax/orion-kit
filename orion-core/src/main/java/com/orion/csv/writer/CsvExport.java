package com.orion.csv.writer;

import com.orion.csv.annotation.ExportField;
import com.orion.csv.annotation.ExportIgnore;
import com.orion.csv.annotation.ExportSetting;
import com.orion.csv.option.CsvWriterOption;
import com.orion.utils.Exceptions;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Maps;
import com.orion.utils.reflect.Annotations;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Csv Bean导出器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/25 10:33
 */
public class CsvExport<T> extends BaseCsvWriter<T, String> {

    private int maxColumnIndex;

    private Class<T> targetClass;

    private boolean addHeader;

    private Map<String, Method> getters;

    private Map<Integer, String> headers = new TreeMap<>();

    public CsvExport(String file, Class<T> targetClass) {
        this(new CsvWriter(file), targetClass);
    }

    public CsvExport(File file, Class<T> targetClass) {
        this(new CsvWriter(file), targetClass);
    }

    public CsvExport(OutputStream out, Class<T> targetClass) {
        this(new CsvWriter(out), targetClass);
    }

    public CsvExport(Writer writer, Class<T> targetClass) {
        this(new CsvWriter(writer), targetClass);
    }

    /**
     * 此方法不会解析setting
     */
    public CsvExport(CsvWriter writer, Class<T> targetClass) {
        super(writer);
        Valid.notNull(this.targetClass = targetClass, "target class is null");
        writer.setOption(parseClass(targetClass));
        this.parseField();
    }

    @Override
    public CsvExport<T> mapping(int column, String field) {
        Method method = getters.get(field);
        if (method == null) {
            throw Exceptions.parse("not found " + field + " getter method");
        }
        mapping.put(column, field);
        maxColumnIndex = Math.max(maxColumnIndex, column);
        return this;
    }

    @Override
    protected String[] parseRow(T row) {
        String[] store;
        if (capacity != -1) {
            store = new String[capacity];
        } else {
            store = new String[maxColumnIndex + 1];
        }
        for (int i = 0; i < store.length; i++) {
            String getter = mapping.get(i);
            if (getter != null) {
                Method method = getters.get(getter);
                Object value = Methods.invokeMethod(row, method);
                if (value != null) {
                    store[i] = Objects1.toString(value);
                } else {
                    store[i] = defaultValue.getOrDefault(i, Strings.EMPTY);
                }
            } else {
                store[i] = defaultValue.getOrDefault(i, Strings.EMPTY);
            }
        }
        return store;
    }

    /**
     * 解析class
     *
     * @param targetClass targetClass
     * @return CsvWriterOption
     */
    protected static CsvWriterOption parseClass(Class<?> targetClass) {
        Valid.notNull(targetClass, "target class is null");
        ExportSetting setting = Annotations.getAnnotation(targetClass, ExportSetting.class);
        if (setting == null) {
            return new CsvWriterOption();
        }
        CsvWriterOption option = new CsvWriterOption()
                .setForceQualifier(setting.forceQualifier());
        option.setTextQualifier(setting.textQualifier())
                .setUseTextQualifier(setting.useTextQualifier())
                .setDelimiter(setting.delimiter())
                .setLineDelimiter(setting.lineDelimiter())
                .setComment(setting.comment())
                .setEscapeMode(setting.escapeMode())
                .setUseTextQualifier(setting.useTextQualifier())
                .setCharset(Charset.forName(setting.charset()));
        return option;
    }

    /**
     * 解析field
     */
    protected void parseField() {
        // 注解field
        List<Field> fieldList = Fields.getFieldByCache(targetClass);
        // 注解method
        List<Method> methodList = Methods.getAllGetterMethodByCache(targetClass);
        this.getters = methodList.stream().collect(Collectors.toMap(Fields::getFieldNameByMethod, Function.identity()));
        for (Field field : fieldList) {
            this.parseColumn(Annotations.getAnnotation(field, ExportField.class),
                    Annotations.getAnnotation(field, ExportIgnore.class),
                    Methods.getGetterMethodByField(targetClass, field), field.getName());
        }
        for (Method method : methodList) {
            this.parseColumn(Annotations.getAnnotation(method, ExportField.class),
                    Annotations.getAnnotation(method, ExportIgnore.class),
                    method, null);
        }
        if (addHeader && headers != null) {
            Optional.ofNullable(Maps.last(headers))
                    .map(Map.Entry::getKey)
                    .map(s -> new String[s + 1])
                    .map(s -> {
                        for (int i = 0; i < s.length; i++) {
                            s[i] = headers.get(i);
                        }
                        return s;
                    })
                    .ifPresent(this::headers);
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
    protected void parseColumn(ExportField field, ExportIgnore ignore, Method method, String fieldName) {
        if (field == null || ignore != null) {
            return;
        }
        if (method == null) {
            throw Exceptions.parse("not found " + fieldName + "getter method");
        }
        int index = field.value();
        String header = field.header();
        if (fieldName == null) {
            mapping.put(index, Fields.getFieldNameByMethod(method));
        } else {
            mapping.put(index, fieldName);
        }
        if (!Strings.isEmpty(header)) {
            addHeader = true;
            headers.put(index, header);
        }
        maxColumnIndex = Math.max(maxColumnIndex, index);
    }

}
