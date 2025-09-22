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
package cn.orionsec.kit.office.excel.reader;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.lang.utils.reflect.Annotations;
import cn.orionsec.kit.lang.utils.reflect.Constructors;
import cn.orionsec.kit.lang.utils.reflect.Fields;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.annotation.ImportField;
import cn.orionsec.kit.office.excel.annotation.ImportIgnore;
import cn.orionsec.kit.office.excel.option.CellOption;
import cn.orionsec.kit.office.excel.option.ImportFieldOption;
import cn.orionsec.kit.office.excel.type.ExcelReadType;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * excel bean 读取器
 * <p>
 * 支持高级数据类型
 * <p>
 * {@link Excels#getCellValue(Cell, ExcelReadType, CellOption)}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/6 17:10
 */
public class ExcelBeanReader<T> extends BaseExcelReader<String, T> {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelBeanReader.class);

    private final Class<T> targetClass;

    private final Constructor<T> constructor;

    private final Map<String, Method> setters;

    /**
     * 如果列为 null 是否调用 setter(null)
     */
    private boolean nullInvoke;

    /**
     * 如果行为 null 是否添加一个新的实例对象
     */
    private boolean nullAddEmptyBean;

    public ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        this(workbook, sheet, targetClass, new ArrayList<>(), null);
    }

    public ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> store) {
        this(workbook, sheet, targetClass, store, null);
    }

    public ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass, Consumer<T> consumer) {
        this(workbook, sheet, targetClass, null, consumer);
    }

    protected ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> rows, Consumer<T> consumer) {
        super(workbook, sheet, rows, consumer);
        this.targetClass = Assert.notNull(targetClass, "target class is null");
        this.constructor = Assert.notNull(Constructors.getDefaultConstructor(targetClass), "target class not found default constructor");
        this.setters = new HashMap<>();
        this.options = new HashMap<>();
        this.analysisField();
        this.init = false;
    }

    public static <T> ExcelBeanReader<T> create(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        return new ExcelBeanReader<>(workbook, sheet, targetClass);
    }

    public static <T> ExcelBeanReader<T> create(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> rows) {
        return new ExcelBeanReader<>(workbook, sheet, targetClass, rows);
    }

    public static <T> ExcelBeanReader<T> create(Workbook workbook, Sheet sheet, Class<T> targetClass, Consumer<T> consumer) {
        return new ExcelBeanReader<>(workbook, sheet, targetClass, consumer);
    }

    /**
     * 如果列为 null 是否调用 setter(null)
     *
     * @return this
     */
    public ExcelBeanReader<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    /**
     * 如果行为 null 是否添加实例对象
     *
     * @return this
     */
    public ExcelBeanReader<T> nullAddEmptyBean() {
        this.nullAddEmptyBean = true;
        return this;
    }

    /**
     * 添加配置
     *
     * @param field  field
     * @param option 配置
     * @return this
     */
    public ExcelBeanReader<T> option(String field, ImportFieldOption option) {
        this.addOption(field, option);
        return this;
    }

    /**
     * 添加配置
     *
     * @param column 列
     * @param field  field
     * @param type   类型
     * @return this
     */
    public ExcelBeanReader<T> option(int column, String field, ExcelReadType type) {
        this.addOption(field, new ImportFieldOption(column, type));
        return this;
    }

    /**
     * 添加配置
     *
     * @param field  field
     * @param option 配置
     */
    @Override
    protected void addOption(String field, ImportFieldOption option) {
        Assert.notNull(option, "field option is null");
        Assert.notNull(setters.get(field), "not found setter method ({}) in {}", field, targetClass);
        super.addOption(field, option);
    }

    @Override
    protected T parserRow(Row row) {
        if (row == null) {
            if (nullAddEmptyBean) {
                return Constructors.newInstance(constructor);
            }
            return null;
        }
        T t = Constructors.newInstance(constructor);
        options.forEach((field, option) -> {
            Method setter = setters.get(field);
            int index = option.getIndex();
            Cell cell = row.getCell(index);
            Object value;
            if (option.getType().equals(ExcelReadType.PICTURE)) {
                // 图片
                value = this.getPicture(setter, row.getRowNum(), index);
            } else {
                // 值
                value = Excels.getCellValue(cell, option.getType(), option.getCellOption());
            }
            if (value != null) {
                if (trim && value instanceof String) {
                    value = ((String) value).trim();
                }
                // 调用 setter
                try {
                    Methods.invokeSetterInfer(t, setter, value);
                } catch (Exception e) {
                    LOGGER.error("ExcelBeanReader.parserRow error", e);
                }
            } else if (nullInvoke) {
                Methods.invokeMethod(t, setter, (Object) null);
            }
        });
        return t;
    }

    /**
     * 读取图片
     *
     * @param setter setter
     * @param row    row
     * @param col    col
     * @return value
     */
    private Object getPicture(Method setter, int row, int col) {
        if (pictureParser == null) {
            return null;
        }
        // 获取图片
        PictureData picture = pictureParser.getPicture(row, col);
        if (picture == null) {
            return null;
        }
        Class<?> parameterType = setter.getParameterTypes()[0];
        if (parameterType == String.class) {
            return Base64s.imgEncode(picture.getData(), picture.getMimeType());
        } else if (parameterType == byte[].class) {
            return picture.getData();
        } else if (parameterType == OutputStream.class || parameterType == ByteArrayOutputStream.class) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                out.write(picture.getData());
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
            return out;
        }
        return null;
    }

    /**
     * 解析field
     */
    private void analysisField() {
        // setter cache
        List<Method> setterMethodList = Methods.getSetterMethodsByCache(targetClass);
        // 扫描field
        List<Field> fieldList = Fields.getFieldsByCache(targetClass);
        for (Field field : fieldList) {
            this.analysisColumn(Annotations.getAnnotation(field, ImportField.class),
                    Annotations.getAnnotation(field, ImportIgnore.class),
                    Methods.getSetterMethodByField(targetClass, field), field.getName());
        }
        // 扫描setter
        for (Method method : setterMethodList) {
            String fieldName = Fields.getFieldNameByMethod(method);
            setters.put(fieldName, method);
            // 解析
            this.analysisColumn(Annotations.getAnnotation(method, ImportField.class),
                    Annotations.getAnnotation(method, ImportIgnore.class),
                    method, fieldName);
        }
    }

    /**
     * 解析 field ignore
     *
     * @param field     field
     * @param ignore    ignore
     * @param method    method
     * @param fieldName fieldName
     */
    private void analysisColumn(ImportField field, ImportIgnore ignore,
                                Method method, String fieldName) {
        if (field == null || ignore != null) {
            return;
        }
        ImportFieldOption option = new ImportFieldOption();
        option.setIndex(field.index());
        option.setType(field.type());
        String parseFormat = field.parseFormat();
        if (!Strings.isEmpty(parseFormat)) {
            option.setCellOption(new CellOption(parseFormat));
        }
        // 解析
        this.analysisColumn(option, fieldName, method);
    }

    /**
     * 解析option
     *
     * @param option    option
     * @param fieldName fieldName
     * @param method    method
     */
    private void analysisColumn(ImportFieldOption option, String fieldName, Method method) {
        Assert.notNull(option, "option is null");
        Assert.notNull(method, fieldName + " setter method not found from " + targetClass);
        Assert.gte(option.getIndex(), 0, "index must >= 0");
        // check
        ExcelReadType type = Objects1.def(option.getType(), ExcelReadType.TEXT);
        option.setType(type);
        // 判断是否支持流式操作
        this.checkStreamingSupportType(type);
        Class<?> parameterType = method.getParameterTypes()[0];
        switch (type) {
            case LINK_ADDRESS:
                // 超链接
                if (!parameterType.equals(String.class)) {
                    throw Exceptions.parse("read hyperlink address parameter type must be String");
                }
                break;
            case PICTURE:
                // 图片
                if (!parameterType.equals(byte[].class)
                        && !parameterType.equals(String.class)
                        && !parameterType.equals(OutputStream.class)
                        && !parameterType.equals(ByteArrayOutputStream.class)) {
                    throw Exceptions.parse("read picture parameter type must be byte[], String, OutputStream or ByteArrayOutputStream");
                }
                break;
            case TEXT:
            default:
                break;
        }
        options.put(fieldName, option);
    }

}
