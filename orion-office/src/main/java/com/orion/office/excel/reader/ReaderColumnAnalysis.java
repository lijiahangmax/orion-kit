package com.orion.office.excel.reader;

import com.orion.able.Analysable;
import com.orion.office.excel.annotation.ImportField;
import com.orion.office.excel.annotation.ImportIgnore;
import com.orion.office.excel.option.CellOption;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.type.ExcelReadType;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.reflect.Annotations;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Excel 读取 列解析器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/2 0:35
 */
public class ReaderColumnAnalysis implements Analysable {

    private Class<?> targetClass;

    private boolean streaming;

    private Map<Method, ImportFieldOption> options;

    public ReaderColumnAnalysis(Class<?> targetClass, boolean streaming, Map<Method, ImportFieldOption> options) {
        this.targetClass = targetClass;
        this.streaming = streaming;
        this.options = options;
    }

    @Override
    public void analysis() {
        // 扫描setter
        List<Method> setterMethodList = Methods.getAllSetterMethod(this.targetClass);
        // 扫描field
        List<Field> fieldList = Fields.getFieldList(this.targetClass);
        for (Field field : fieldList) {
            this.analysisColumn(Annotations.getAnnotation(field, ImportField.class),
                    Annotations.getAnnotation(field, ImportIgnore.class),
                    Methods.getSetterMethodByField(targetClass, field), field.getName());
        }
        for (Method method : setterMethodList) {
            this.analysisColumn(Annotations.getAnnotation(method, ImportField.class),
                    Annotations.getAnnotation(method, ImportIgnore.class),
                    method, null);
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
        option.setParseFormat(parseFormat);
        if (!Strings.isEmpty(parseFormat)) {
            option.setCellOption(new CellOption(parseFormat));
        }
        this.analysisColumn(option, fieldName, method);
    }

    /**
     * 解析option
     *
     * @param option    option
     * @param fieldName fieldName
     * @param method    method
     */
    public void analysisColumn(ImportFieldOption option, String fieldName, Method method) {
        Valid.notNull(option, "option is null");
        Valid.notNull(method, fieldName + " setter method not found from " + targetClass);
        Valid.gte(option.getIndex(), 0, "index must >= 0");
        // check
        ExcelReadType type = option.getType();
        if (type == null) {
            option.setType(type = ExcelReadType.TEXT);
        }
        if (streaming && (type.equals(ExcelReadType.LINK_ADDRESS) ||
                type.equals(ExcelReadType.COMMENT) ||
                type.equals(ExcelReadType.PICTURE))) {
            throw Exceptions.parse("streaming just support read value");
        }
        Class<?> parameterType = method.getParameterTypes()[0];
        switch (type) {
            case LINK_ADDRESS:
                if (!parameterType.equals(String.class)) {
                    throw Exceptions.parse("read hyperlink address parameter type must be String");
                }
                break;
            case PICTURE:
                if (!parameterType.equals(byte[].class) &&
                        !parameterType.equals(String.class) &&
                        !parameterType.equals(OutputStream.class) &&
                        !parameterType.equals(ByteArrayOutputStream.class)) {
                    throw Exceptions.parse("read picture parameter type must be byte[], String, OutputStream or ByteArrayOutputStream");
                }
                break;
            case TEXT:
            default:
                break;
        }
        options.put(method, option);
    }

}
