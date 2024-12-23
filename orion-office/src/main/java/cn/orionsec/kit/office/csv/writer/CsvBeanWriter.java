/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.office.csv.writer;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.reflect.Annotations;
import cn.orionsec.kit.lang.utils.reflect.Fields;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.office.csv.annotation.ExportField;
import cn.orionsec.kit.office.csv.annotation.ExportIgnore;
import cn.orionsec.kit.office.csv.annotation.ExportSetting;
import cn.orionsec.kit.office.csv.core.CsvWriter;
import cn.orionsec.kit.office.csv.option.CsvWriterOption;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * csv bean 导出器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/25 10:33
 */
public class CsvBeanWriter<T> extends BaseCsvWriter<String, T> {

    /**
     * targetClass
     */
    private final Class<T> targetClass;

    /**
     * 是否需要添加注解表头
     */
    private boolean addHeader;

    /**
     * 是否将 index 作为排序字段
     */
    private boolean indexToSort;

    /**
     * all getter
     */
    private Map<String, Method> getters;

    /**
     * 注解表头
     */
    private Map<Integer, String> headers;

    public CsvBeanWriter(String file, Class<T> targetClass) {
        this(new CsvWriter(file), targetClass);
    }

    public CsvBeanWriter(File file, Class<T> targetClass) {
        this(new CsvWriter(file), targetClass);
    }

    public CsvBeanWriter(OutputStream out, Class<T> targetClass) {
        this(new CsvWriter(out), targetClass);
    }

    public CsvBeanWriter(Writer writer, Class<T> targetClass) {
        this(new CsvWriter(writer), targetClass);
    }

    public CsvBeanWriter(CsvWriter writer, Class<T> targetClass) {
        super(writer);
        this.targetClass = Valid.notNull(targetClass, "target class is null");
        this.headers = new TreeMap<>();
        this.parseClass();
        this.parseField();
    }

    public static <T> CsvBeanWriter<T> create(String file, Class<T> targetClass) {
        return new CsvBeanWriter<>(file, targetClass);
    }

    public static <T> CsvBeanWriter<T> create(File file, Class<T> targetClass) {
        return new CsvBeanWriter<>(file, targetClass);
    }

    public static <T> CsvBeanWriter<T> create(OutputStream out, Class<T> targetClass) {
        return new CsvBeanWriter<>(out, targetClass);
    }

    public static <T> CsvBeanWriter<T> create(Writer writer, Class<T> targetClass) {
        return new CsvBeanWriter<>(writer, targetClass);
    }

    public static <T> CsvBeanWriter<T> create(CsvWriter writer, Class<T> targetClass) {
        return new CsvBeanWriter<>(writer, targetClass);
    }

    @Override
    public CsvBeanWriter<T> mapping(int column, String field) {
        Method method = getters.get(field);
        if (method == null) {
            throw Exceptions.parse("not found " + field + " getter method");
        }
        return (CsvBeanWriter<T>) super.mapping(column, field);
    }

    @Override
    protected String[] parseRow(T row) {
        String[] store = super.capacityStore();
        for (int i = 0; i < store.length; i++) {
            String getter = mapping.get(i);
            if (getter == null) {
                continue;
            }
            // 执行getter
            Method method = getters.get(getter);
            Object value = Methods.invokeMethod(row, method);
            if (value != null) {
                store[i] = Objects1.toString(value);
            } else {
                store[i] = Objects1.toString(defaultValue.get(getter));
            }
        }
        return store;
    }

    /**
     * 解析class
     */
    protected void parseClass() {
        ExportSetting setting = Annotations.getAnnotation(targetClass, ExportSetting.class);
        CsvWriterOption option = new CsvWriterOption();
        if (setting == null) {
            writer.setOption(option);
            return;
        }
        option.setForceQualifier(setting.forceQualifier())
                .setTextQualifier(setting.textQualifier())
                .setUseTextQualifier(setting.useTextQualifier())
                .setDelimiter(setting.delimiter())
                .setLineDelimiter(setting.lineDelimiter())
                .setComment(setting.comment())
                .setEscapeMode(setting.escapeMode())
                .setUseTextQualifier(setting.useTextQualifier())
                .setCharset(Charset.forName(setting.charset()))
                .setTrim(setting.trim());
        writer.setOption(option);
        this.indexToSort = setting.indexToSort();
    }

    /**
     * 解析field
     */
    protected void parseField() {
        // 注解field
        List<Field> fieldList = Fields.getFieldsByCache(targetClass);
        // 注解method
        List<Method> methodList = Methods.getGetterMethodsByCache(targetClass);
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
        // 设置索引为排序
        this.indexToSort();
        // 设置表头
        if (addHeader && headers != null) {
            Optional.ofNullable(Maps.last(headers))
                    .map(Map.Entry::getKey)
                    .map(s -> new String[s + 1])
                    .map(s -> {
                        for (int i = 0; i < s.length; i++) {
                            s[i] = headers.get(i);
                        }
                        return s;
                    }).ifPresent(this::headers);
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
            this.addHeader = true;
            headers.put(index, header);
        }
        this.maxColumnIndex = Math.max(maxColumnIndex, index);
    }

    /**
     * 将index作为排序字段
     */
    protected void indexToSort() {
        if (!indexToSort) {
            return;
        }
        // mapping
        Map<Integer, String> sortMapping = new TreeMap<>();
        // header
        TreeMap<Integer, String> sortHeaders = new TreeMap<>();
        int i = 0;
        Set<Map.Entry<Integer, String>> mappingEntities = mapping.entrySet();
        for (Map.Entry<Integer, String> mappingEntity : mappingEntities) {
            sortMapping.put(i, mappingEntity.getValue());
            sortHeaders.put(i, headers.get(mappingEntity.getKey()));
            i++;
        }
        // 设置mapping 和 header
        super.mapping = sortMapping;
        this.headers = sortHeaders;
        // 设置 maxColumnIndex
        this.maxColumnIndex = mappingEntities.size() - 1;
    }

}
