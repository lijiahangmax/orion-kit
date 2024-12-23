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
package cn.orionsec.kit.office.csv.reader;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.reflect.Annotations;
import cn.orionsec.kit.lang.utils.reflect.Constructors;
import cn.orionsec.kit.lang.utils.reflect.Fields;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.office.csv.annotation.ImportField;
import cn.orionsec.kit.office.csv.annotation.ImportIgnore;
import cn.orionsec.kit.office.csv.annotation.ImportSetting;
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.option.CsvReaderOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * csv bean 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/7 14:50
 */
public class CsvBeanReader<T> extends BaseCsvReader<T> {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvBeanReader.class);

    private final Class<T> targetClass;

    private Constructor<T> constructor;

    /**
     * 如果列为 null 是否调用 setter(null)
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
     * 如果列为 null 是否调用 setter(null)
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
            // 调用 setter
            if (value != null) {
                try {
                    Methods.invokeSetterInfer(t, setter, value);
                } catch (Exception e) {
                    LOGGER.error("CsvBeanReader.parserRow error", e);
                }
            } else if (nullInvoke) {
                Methods.invokeMethod(t, setter, (Object) null);
            }
        });
        return t;
    }

    /**
     * 解析 class
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
     * 解析 field
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
