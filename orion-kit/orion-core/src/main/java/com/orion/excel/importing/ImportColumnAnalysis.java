package com.orion.excel.importing;

import com.orion.able.Analysable;
import com.orion.excel.annotation.ImportField;
import com.orion.excel.annotation.ImportIgnore;
import com.orion.excel.option.CellOption;
import com.orion.excel.option.ImportFieldOption;
import com.orion.excel.option.ImportSheetOption;
import com.orion.excel.type.ExcelReadType;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
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
 * Import 列 解析器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/2 0:35
 */
public class ImportColumnAnalysis implements Analysable {

    private Class<?> targetClass;

    private ImportSheetOption<?> sheetOption;

    private Map<Method, ImportFieldOption> fieldOptions;

    public ImportColumnAnalysis(Class<?> targetClass, ImportSheetOption<?> sheetOption, Map<Method, ImportFieldOption> fieldOptions) {
        this.targetClass = targetClass;
        this.sheetOption = sheetOption;
        this.fieldOptions = fieldOptions;
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
        if (method == null) {
            throw Exceptions.parse("not found " + fieldName + "setter method");
        }
        ImportFieldOption fieldOption = new ImportFieldOption();
        // check
        if (sheetOption.isStreaming() && (field.type().equals(ExcelReadType.LINK_ADDRESS) ||
                field.type().equals(ExcelReadType.COMMENT) ||
                field.type().equals(ExcelReadType.PICTURE))) {
            throw Exceptions.parse("streaming just support read value");
        }
        Class<?> parameterType = method.getParameterTypes()[0];
        switch (field.type()) {
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
                sheetOption.setHavePicture(true);
                break;
            case TEXT:
            default:
                break;
        }
        fieldOption.setIndex(field.index());
        fieldOption.setType(field.type());
        String parseFormat = field.parseFormat();
        fieldOption.setParseFormat(parseFormat);
        if (!Strings.isEmpty(parseFormat)) {
            fieldOption.setCellOption(new CellOption(parseFormat));
        }
        fieldOptions.put(method, fieldOption);
    }

}
